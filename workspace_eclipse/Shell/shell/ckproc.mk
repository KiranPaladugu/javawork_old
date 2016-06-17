CFLAGS	      = -Aa -D_HPUX_SOURCE +DAportable -D_PSTAT64

DEST	      = .

EXTHDRS	      =

HDRS	      =

INSTALL	      = /usr/sbin/install

CC	      = aCC

LD	      = aCC

LDFLAGS	      =

LIBS	      =

MAKEFILE      = ckproc.mk

OBJS	      = ckproc.o

PRINT	      = pr

PROGRAM       = ckproc

SHELL	      = /usr/bin/sh

SRCS	      = ckproc.c

SYSHDRS	      = /usr/include/.unsupp/sys/_errno.h \
		/usr/include/.unsupp/sys/_signal.h \
		/usr/include/.unsupp/sys/_stat.h \
		/usr/include/.unsupp/sys/_types.h \
		/usr/include/.unsupp/sys/_unistd.h \
		/usr/include/errno.h \
		/usr/include/libgen.h \
		/usr/include/machine/frame.h \
		/usr/include/machine/save_state.h \
		/usr/include/machine/sys/sigcontext.h \
		/usr/include/machine/sys/syscall.h \
		/usr/include/machine/vmtypes.h \
		/usr/include/pwd.h \
		/usr/include/stdio.h \
		/usr/include/stdlib.h \
		/usr/include/string.h \
		/usr/include/sys/_fd_macros.h \
		/usr/include/sys/_inttypes.h \
		/usr/include/sys/_mbstate_t.h \
		/usr/include/sys/_null.h \
		/usr/include/sys/_rlimit_body.h \
		/usr/include/sys/_rlimit_body.h \
		/usr/include/sys/_size_t.h \
		/usr/include/sys/_stat_body.h \
		/usr/include/sys/_stat_body.h \
		/usr/include/sys/_time_t.h \
		/usr/include/sys/_wchar_t.h \
		/usr/include/sys/errno.h \
		/usr/include/sys/newsig.h \
		/usr/include/sys/pstat.h \
		/usr/include/sys/pstat/crash_pstat_body.h \
		/usr/include/sys/pstat/disk_pstat_body.h \
		/usr/include/sys/pstat/fid_pstat_body.h \
		/usr/include/sys/pstat/filedetails_pstat_body.h \
		/usr/include/sys/pstat/global_pstat_body.h \
		/usr/include/sys/pstat/ipc_pstat_body.h \
		/usr/include/sys/pstat/lv_pstat_body.h \
		/usr/include/sys/pstat/lwp_pstat_body.h \
		/usr/include/sys/pstat/mdep_pstat_body.h \
		/usr/include/sys/pstat/node_pstat_body.h \
		/usr/include/sys/pstat/pm_pstat_body.h \
		/usr/include/sys/pstat/pset_pstat_body.h \
		/usr/include/sys/pstat/pstat_body.h \
		/usr/include/sys/pstat/pstat_constants.h \
		/usr/include/sys/pstat/pstat_ops.h \
		/usr/include/sys/pstat/pstat_vfs_types.h \
		/usr/include/sys/pstat/rpath_pstat_body.h \
		/usr/include/sys/pstat/socket_pstat_body.h \
		/usr/include/sys/pstat/stream_pstat_body.h \
		/usr/include/sys/pstat/vfs_pstat_body.h \
		/usr/include/sys/pstat/vm_pstat_body.h \
		/usr/include/sys/resource.h \
		/usr/include/sys/scall_define.h \
		/usr/include/sys/sigevent.h \
		/usr/include/sys/siginfo.h \
		/usr/include/sys/signal.h \
		/usr/include/sys/stat.h \
		/usr/include/sys/stdsyms.h \
		/usr/include/sys/syscall.h \
		/usr/include/sys/time.h \
		/usr/include/sys/types.h \
		/usr/include/sys/unistd.h \
		/usr/include/sys/wait.h \
		/usr/include/unistd.h \
		/usr/include/utime.h

all:		$(PROGRAM)

$(PROGRAM):     $(OBJS) $(LIBS)
		@echo "Linking $(PROGRAM) ..."
		@chmod 644 $(OBJS)
		@$(LD) $(LDFLAGS) $(OBJS) $(LIBS) -o $(PROGRAM)
		@chmod 755 $(PROGRAM)
		@-strip $(PROGRAM)
		@echo "done"

clean:;		@rm -f $(OBJS) core $(PROGRAM)

clobber:;	@rm -f $(OBJS) $(PROGRAM) core tags

depend:;	@mkmf -f $(MAKEFILE) ROOT=$(ROOT)

echo:;		@echo $(HDRS) $(SRCS)

index:;		@ctags -wx $(HDRS) $(SRCS)

install:	$(PROGRAM)
		@echo Installing $(PROGRAM) in $(DEST)
		@-strip $(PROGRAM)
		@if [ $(DEST) != . ]; then \
		(rm -f $(DEST)/$(PROGRAM); $(INSTALL) -f $(DEST) $(PROGRAM)); fi

print:;		@$(PRINT) $(HDRS) $(SRCS)

tags:           $(HDRS) $(SRCS); @ctags $(HDRS) $(SRCS)

update:		$(DEST)/$(PROGRAM)

$(DEST)/$(PROGRAM): $(SRCS) $(LIBS) $(HDRS) $(EXTHDRS)
		@$(MAKE) -f $(MAKEFILE) ROOT=$(ROOT) DEST=$(DEST) install
###
ckproc.o: /usr/include/stdio.h /usr/include/sys/stdsyms.h \
	/usr/include/sys/types.h /usr/include/sys/_inttypes.h \
	/usr/include/machine/vmtypes.h /usr/include/sys/_fd_macros.h \
	/usr/include/.unsupp/sys/_types.h /usr/include/sys/_mbstate_t.h \
	/usr/include/sys/_null.h /usr/include/sys/_size_t.h \
	/usr/include/stdlib.h /usr/include/sys/_wchar_t.h \
	/usr/include/sys/wait.h /usr/include/sys/resource.h \
	/usr/include/sys/time.h /usr/include/sys/sigevent.h \
	/usr/include/sys/_rlimit_body.h /usr/include/sys/_rlimit_body.h \
	/usr/include/sys/signal.h /usr/include/sys/siginfo.h \
	/usr/include/sys/newsig.h /usr/include/machine/save_state.h \
	/usr/include/machine/frame.h /usr/include/sys/syscall.h \
	/usr/include/sys/scall_define.h /usr/include/machine/sys/syscall.h \
	/usr/include/machine/sys/sigcontext.h \
	/usr/include/.unsupp/sys/_signal.h /usr/include/pwd.h \
	/usr/include/errno.h /usr/include/sys/errno.h \
	/usr/include/.unsupp/sys/_errno.h /usr/include/string.h \
	/usr/include/libgen.h /usr/include/unistd.h /usr/include/sys/unistd.h \
	/usr/include/utime.h /usr/include/sys/_time_t.h \
	/usr/include/.unsupp/sys/_unistd.h /usr/include/sys/stat.h \
	/usr/include/sys/_stat_body.h /usr/include/sys/_stat_body.h \
	/usr/include/.unsupp/sys/_stat.h /usr/include/sys/pstat.h \
	/usr/include/sys/pstat/pstat_ops.h \
	/usr/include/sys/pstat/pstat_constants.h \
	/usr/include/sys/pstat/pstat_body.h \
	/usr/include/sys/pstat/pstat_vfs_types.h \
	/usr/include/sys/pstat/fid_pstat_body.h \
	/usr/include/sys/pstat/filedetails_pstat_body.h \
	/usr/include/sys/pstat/stream_pstat_body.h \
	/usr/include/sys/pstat/socket_pstat_body.h \
	/usr/include/sys/pstat/rpath_pstat_body.h \
	/usr/include/sys/pstat/pm_pstat_body.h \
	/usr/include/sys/pstat/global_pstat_body.h \
	/usr/include/sys/pstat/vm_pstat_body.h \
	/usr/include/sys/pstat/disk_pstat_body.h \
	/usr/include/sys/pstat/ipc_pstat_body.h \
	/usr/include/sys/pstat/lv_pstat_body.h \
	/usr/include/sys/pstat/vfs_pstat_body.h \
	/usr/include/sys/pstat/mdep_pstat_body.h \
	/usr/include/sys/pstat/pset_pstat_body.h \
	/usr/include/sys/pstat/crash_pstat_body.h \
	/usr/include/sys/pstat/node_pstat_body.h \
	/usr/include/sys/pstat/lwp_pstat_body.h
