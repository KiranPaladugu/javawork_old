#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libgen.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/pstat.h>

#define BURST  10

/******************************************************************************/
int     CheckProcess(char *FullPrcName)
/******************************************************************************/
{
  struct pst_status  l_pst[BURST];
  int                l_i, l_count, l_len, l_ret = 0;
  long               l_idx = 0;
  struct stat        l_buf;
  char              *l_baseName = basename(FullPrcName);

  if(access(FullPrcName, X_OK))
    return(-1);

  while ((l_count = pstat_getproc(l_pst, sizeof(l_pst[0]),
                                  BURST, (int)l_idx)) > 0)
    {
      for (l_i = 0; l_i < l_count; l_i++)
        {
          l_len = strlen(l_baseName);
          if(l_len > (PST_UCOMMLEN - 1)) l_len = PST_UCOMMLEN - 1;
          if(!strncmp(l_baseName, l_pst[l_i].pst_ucomm, l_len))
            if(!stat(FullPrcName, &l_buf))
              {
                if(((long)l_buf.st_ino == (long)l_pst[l_i].pst_text.psf_fileid) && ((long)l_buf.st_dev == (long)l_pst[l_i].pst_text.psf_fsid.psfs_id))
                  return((int)l_pst[l_i].pst_pid);
              }
            else
              return(-1);
        }
      l_idx = l_pst[l_count-1].pst_idx + 1;
    }

  if(l_count == -1)
    return(-1);

  return(l_ret);
}

int main(int argc, char *argv[])
{
  extern char *optarg;
  extern int   optind, optopt;
  int          l_c, l_errflg = 0, l_result;
  char        *l_fileName = (char *)0;

  opterr = 0;
  while ((l_c = getopt(argc, argv, ":f:h")) != -1)
    switch (l_c) {
      case 'h':
        l_errflg++;
        break;
      case 'f':
        l_fileName = optarg;
        break;
      case ':':        /* -f */
        printf("\nOption -%c requires argument FileName\n", optopt);
        fflush(stdout);
        l_errflg++;
        break;
      case '?':
        printf("\nUnrecognized option: - %c\n", optopt);
        fflush(stdout);
        l_errflg++;
        break;
      default:
        printf("\n????? Unrecognized option: - %c\n", optopt);
        fflush(stdout);
        l_errflg++;
       }

  if(l_errflg || !l_fileName || (l_fileName[0] == '-'))
    {
        printf("\nUsage: %s -f file  -> check if file is Running\n",argv[0]);
        printf("Usage: %s -h       -> for help\n\n",argv[0]);
        fflush(stdout);
    }

  l_result = CheckProcess(l_fileName);

  if(l_result == -1)
    return(1);

  printf("%d\n", l_result);
  fflush(stdout);

  return(0);
}
