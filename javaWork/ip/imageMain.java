// imageMain.java   --- the "array of operations" implementation
//
// This main() method illustrates how to perform image operations:
//    - read an image from an input file.  The user is asked to type a
//      file name.  The file can be in gif, jpeg or bmp format.
//    - modify the image
//             apply a green filter to the top half,
//             apply a fading green filter across the whole image, 
//             add vertical black bars to the image,
//             convert to a grayscale image
//             etc.
//             write an image to a bmp output file
//             quit
// The user repeatedly selects which operations to apply.
//
// In this implementation, the operations are defined using an array.
// Each element in the ops array is an instance of the abstract class ImageOp.
// This class contains two strings and a method.  One string is the menu
// description.  The other string contains the text that the user must
// type in order to select the operation.  The method contains the code
// that carries out the operation.
// Some advantages of using an array in this way:
//    - When a new image operation is added, there is only ONE place in
//      the code that has to be updated.  A new element in the ops array
//      must be defined: in this part of the source code, all
//      three components of an image-operation are defined.
//      (In contrast, using the original code organization, the menu string
//      has to be typed in one part of the source code, the "user-string-
//      to-select-an-operation" has to be typed in another part of the source
//      code, and the method to implement the operation has to be typed in
//      yet another part of the source code.)
//    - The image operations can easily be reordered.  Just swap the
//      two elements in the initialization of the ops array.
//      (In contrast, using the original code organization, several places
//      in the code have to be changed to reorder image operations.)
//
// Within the Java code, a colour image is represented using a 
// three-dimensional array.
//    The first index selects one row of the image.
//    The second index selects one column of the image.
//    The third index selects a colour (RED, GREEN, or BLUE).
// For example, "testImage[3][5][RED]" is an integer value in the range
// 0 to 255.  A 0 indicates that there is no red colour at row 3, column 5
// in testImage.  A 255 indicates that there is maximal red colour at
// this location.
// If RED, GREEN, and BLUE are all 0, the image appears black.  If they
// are all 255, the image appears white.
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class imageMain {
  
  // Give names to the constants used for the red, green and blue 
  // values in an image.
  // The use of final variables is discussed on page 10-11 of the
  // Main textbook used in CISC124.
  // Red, green and blue are 8 bit quantities, each with a value 
  // between 0 and 255.  In a file, each pixel occupies a 32 bit word.
  // Since red, green and blue need a total of 24 bits, this leaves
  // 8 extra bits.  These extra bits are called "offset".  You never need
  // to change the value of the offset bits.
  final static int RED    = 0;
  final static int GREEN  = 1;
  final static int BLUE   = 2;
  final static int OFFSET = 3;  // ignore offset; use only red, green, blue
  
  
  // The console is used to receive input typed by the user.  In this program,
  // the user is asked to type file names and command names.
  static public BufferedReader console 
         = new BufferedReader(new InputStreamReader(System.in));



  // --------------------------------- main -----------------------------
  // The main method reads an input image file, and allows the user to 
  // select from a set of modifications that can be applied to the image.
  
  
  public static void main(String[] args) {
    // Declare testImage, which is used to store an image as a 3D array.
    int[][][] testImage;
    
    System.out.println("Welcome to the Image Program.");
    System.out.println("");

        // This program is supposed to be run without any arguments.
        // If the user supplies arguments, issue an explanatory message.
        if (args.length!=0) {
           System.out.println("Usage: java imageProgram");
           System.exit(1);  // exit.  The exit code 1 indicates failure
        }

        // Ask the user for a file name and read the contents of this 
        // file into "testImage".
        // After method "loadImage" completes, the size of the image 
        // is available as follows:
        //    - the number of rows is testImage.length
        //    - the number of columns is testImage[0].length
        testImage = imageIO.loadImage();



        // Repeatedly offer the user a list of operations to choose from.
        // The set of operations is defined in array ops.  This array
        // is declared and initialized below.
        while (true) {    
            System.out.println("");
            System.out.println("What operation do you want to perform? ");

            // Go through the ops array to print information about each
            // of the available operations.  The getMenuString() method 
            // provides a string that describes the image operation.
            // The getTypeThis() method provides a string that the
            // user is supposed to type to select the image operation.
            // Print the menu items two per line.
            for(int i=0; i<ops.length; i++) {
                String menuString = ops[i].getMenuString();
                String typeThis = ops[i].getTypeThis();
                System.out.print("    "+typeThis);
                // Print enough spaces to make the menuString line up
                for (int j=typeThis.length(); j<10;j++)
                    System.out.print(' ');
                System.out.print(menuString);
                if (i%2 == 0)
                    // print spaces preceding the second column of menu items.
                    for (int j=menuString.length(); j<26;j++) 
                        System.out.print(' ');
                else
                    System.out.println();
            } // for i
            if (ops.length % 2 == 1) System.out.println();

            // Read the users reply.
            String reply = null;
            try{
                reply = console.readLine();
            }
            catch(IOException e) {
                System.out.println(e);
                System.exit(1);
            }


            // Go through the ops array looking for a typeThis string that
            // matches what the user typed.  First convert the reply to
            // upper case so that the comparison is not case-sensitive.
            reply = reply.toUpperCase();
            boolean foundit = false;
            for (int i=0; i<ops.length; i++) {
                if (reply.equals(ops[i].getTypeThis())) {
                    foundit = true;
                    // Call the op() method to carry out this operation.
                    ops[i].op(testImage);
                } // if reply.equals
            } // for i

            // Print a message if the user's reply was not recognized as 
            // a command.
            if (!foundit)
                System.out.println("Unrecognized command.  Please try again.");
        } // end of "while true"
  } // end of main()






  //               ********* Declare the ops array ********
  // This is an array of ImageOp classes.  The ImageOp class (an abstract
  // class) is instantiated in a different way for each image operation.
  // 
  // The following large chunk of code is a huge array initialization.
  // Here is the syntax for a small-scale array initialization:
  //       int numArray[] = { 1, 4, 0, 32};
  // This declares an integer array called numArray.  It allocates space
  // for this array: 4 items, with initial values 1, 4, 0, 32.  The number
  // of entries in the array is determined by the java compiler: the compiler
  // counts how many items are given in the initial-values list.
  // Looking at the code below, you see a declaration for an ImageOp array 
  // called ops.  This array has a lengthy initial-values list.  Each
  // entry in the intial-values list is an instance of the abstract class
  // ImageOp.  The abstract class is instantiated via "new ImageOp".
  // The two parameters (which go to the constructor for ImageOp) are
  // strings: the string that describes the menu item to the user, and the
  // string that the user must type to select the operation.
  // Those two strings are followed by code for the abstract
  // method "op".  For each image operation, code for the "op" method is 
  // different, to define the behaviour of that particular operation.
  private static ImageOp ops[] = {
    // ========================= topHalfGreen ===============================
    // Make the top half of the image green, while still allowing
    // red and blue to show through.  This is done by visiting
    // rows 0 to MAXROWS/2.  At each visited pixel, the GREEN value 
    // is set to its maximum possible value, which is 255.
    new ImageOp("Green filter in top half", "TOPGREEN") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            final int MAXCOLS = img[0].length;
            for (int row=0; row<MAXROWS/2; row++) {
                for (int col=0; col<MAXCOLS; col++) {
                    img[row][col][GREEN] = 255;
                }  // for col
            }  // for row
           System.out.println("The top half of the image is green.");
       } // end of op() for topHalfGreen
    },  // end of new ImageOp

    // ========================= fadingGreenFilter =========================
    // Apply a fading green filter to the image.  Green is weak on the left
    // and strong on the right.
    new ImageOp("Fading green filter", "FADEGREEN") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            final int MAXCOLS = img[0].length;
            double scaleFactor = 255.0/MAXCOLS;
            for (int row=0; row<MAXROWS; row++) {
                for (int col=0; col<MAXCOLS; col++) {
                    img[row][col][GREEN] = (int)(col*scaleFactor);
                }  // for col
            }  // for row
            System.out.println("A fading green filter has been applied.");
        } // end of op() for fadingGreenFilter
    }, // end of new ImageOp

    // ========================= verticalBlackBars =========================
    // Put vertical bars across the image.  
    // Use the modulus operator % to decide which columns to make black.
    new ImageOp("Add vertical black bars", "VERTBARS") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            final int MAXCOLS = img[0].length;
            for (int row=0; row<MAXROWS; row++) {
                for (int col=0; col<MAXCOLS; col++) {
                    if (col % 30 > 22) { 
                        img[row][col][RED]   = 0;
                        img[row][col][GREEN] = 0;
                        img[row][col][BLUE]  = 0;
                    }  // if col
                }  // for col
            }  // for row
            System.out.println("Vertical bars have been added.");
        } // end of op() for verticalBlackBars
      }, // end of new ImageOp

    // ========================= grayScale ===============================
    // Convert the image from colour to grayscale.
    new ImageOp("Convert to gray scale", "GRAY") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            final int MAXCOLS = img[0].length;
            for (int row=0; row<MAXROWS; row++) {
                for (int col=0; col<MAXCOLS; col++) {
                    int avg = (img[row][col][RED] +
                               img[row][col][BLUE] +
                               img[row][col][GREEN]) / 3;
                    img[row][col][RED]   = avg;
                    img[row][col][GREEN] = avg;
                    img[row][col][BLUE]  = avg;
                }  // for col
            }  // for row
            System.out.println("The image is now grayscale.");
        } // end of op() for grayScale
    }, // end of new ImageOp

      
    // ========================= diagonalBlackBars =======================
    // Put diagonal black bars across the image, going from
    // top left to bottom right.  
    new ImageOp("Add diagonal black bars", "DIAGBARS") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            final int MAXCOLS = img[0].length;
            for (int row=0; row<MAXROWS; row++) {
                for (int col=0; col<MAXCOLS; col++) {
                    int mod = (col-row) % 30;
                    if (mod < 0) mod += 30;  // make "mod" be positive
                    if (mod > 22) { 
                        img[row][col][RED]   = 0;
                        img[row][col][GREEN] = 0;
                        img[row][col][BLUE]  = 0;
                    }  // if mod
                }  // for col
            }  // for row
            System.out.println("Diagonal bars have been added.");
        } // end of op() for  diagonalBlackBars
    }, //  end of new ImageOp

    // ========================= flipUpsideDown ========================
    new ImageOp("Flip upside down", "FLIP") {
        public void op(int[][][] img) {
            final int MAXROWS = img.length;
            for (int row=0; row<MAXROWS/2; row++) {
                int[][] temp = img[row];
                img[row] = img[MAXROWS-row-1];
                img[MAXROWS-row-1] = temp;
             }  // for row
             System.out.println("Image has been flipped upside down");
         } // end of op() for flipUpsideDown
    }, // end of new ImageOp

    // ========================= save ===============================
    // Save the image to a file.
    new ImageOp("Save as bmp file", "SAVE") {
        public void op(int[][][] img) {
            imageIO.saveImage(img);
        }  // end of op() for save
    }, // end of new ImageOp

    // ========================= exitProgram ===============================
    // Close the console and exit the program 
    new ImageOp("quit", "QUIT") {
        public void op(int[][][] img) {
            try{
                console.close();
            } catch(IOException e) {
                System.out.println(e);
                System.exit(1);
            }
            System.exit(0);
        } // end of op() for exitProgram
    } // end of new ImageOp
  }; // end of ops[] definition
} // end of imageMain()


 
//               ********* abstract class ImageOp ********
// The ImageOp class describes a generic image operation.
// Three methods are defined:
//  getMenuString -- returns a string that describes the image operation to
//                   the user.
//  getTypeThis   -- returns the string that the user has to type in order
//                   to execute the image operation
//  op -- an abstract method which carries out the image operation.
//        Every time that a class is derived from abstract class ImageOp,
//        code must be provided for the op method.
abstract class ImageOp {
  protected String menuString; // This string describes the image operation 
                               // to the user.
  protected String typeThis;   // This is the string the user types to select
                               // this image operation.

  // The constructor for abstract class ImageOp.  
  // The menuString and typeThis strings are initialized here.
  public ImageOp(String ms, String tt) {
    menuString = ms;
    typeThis = tt;
  }

  // getMenuString() returns the descriptive string.
  public String getMenuString() { return menuString; }

  // getTypeThis() returns the string that the user is supposed
  // to type (to select this image operation).
  public String getTypeThis() { return typeThis; }

  // The op() method carries out the image operation.  For now, define
  // it as an abstract method that takes an image as a parameter and
  // returns void.  The actual steps carried out by op() are defined
  // when a class is derived from abstract class ImageOp.  This is done 
  // in the code above which defines the ops[] array.
  abstract void op(int[][][] img);
}  // end of abstract class ImageOp





