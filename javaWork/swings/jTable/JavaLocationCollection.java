package jTable;

/** A simple collection that stores multiple JavaLocation
 *  objects in an array and determines the number of
 *  unique countries represented in the data.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */

public class JavaLocationCollection {
  private static JavaLocation[] defaultLocations =
    { new JavaLocation("Belgium",
                       "near Liege",
                       "flags/belgium.gif"),
      new JavaLocation("Brazil",
                       "near Salvador",
                       "flags/brazil.gif"),
      new JavaLocation("Colombia",
                       "near Bogota",
                       "flags/colombia.gif"),
      new JavaLocation("Indonesia",
                       "main island",
                       "flags/indonesia.gif"),
      new JavaLocation("Jamaica",
                       "near Spanish Town",
                       "flags/jamaica.gif"),
      new JavaLocation("Mozambique",
                       "near Sofala",
                       "flags/mozambique.gif"),
      new JavaLocation("Philippines",
                       "near Quezon City",
                       "flags/philippines.gif"),
      new JavaLocation("Sao Tome",
                       "near Santa Cruz",
                       "flags/saotome.gif"),
      new JavaLocation("Spain",
                       "near Viana de Bolo",
                       "flags/spain.gif"),
      new JavaLocation("Suriname",
                       "near Paramibo",
                       "flags/suriname.gif"),
      new JavaLocation("United States",
                       "near Montgomery, Alabama",
                       "flags/usa.gif"),
      new JavaLocation("United States",
                       "near Needles, California",
                       "flags/usa.gif"),
      new JavaLocation("United States",
                       "near Dallas, Texas",
                       "flags/usa.gif")
    };

  private JavaLocation[] locations;
  private int numCountries;

  public JavaLocationCollection(JavaLocation[] locations) {
    this.locations = locations;
    this.numCountries = countCountries(locations);
  }
  
  public JavaLocationCollection() {
    this(defaultLocations);
  }

  public JavaLocation[] getLocations() {
    return(locations);
  }

  public int getNumCountries() {
    return(numCountries);
  }

  // Count the number of unique countries in the data.
  // Assumes the list is sorted by country name
  private int countCountries(JavaLocation[] locations) {
    int n = 0;
    String currentCountry, previousCountry = "None";
    for(int i=0;i<locations.length;i++) {
      currentCountry = locations[i].getCountry();
      if (!previousCountry.equals(currentCountry)) {
        n++;
     }
      currentCountry = previousCountry;
    }
    return(n);
  }
}