/**
VUWriter - by Eduardo Maltez

This class is what produces the .ini file
to create a Rainmeter VU meter
*/

/*============
IMPORTS
============*/
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;



public class VUWriter
{
    private static PrintWriter VUScript;
    private static String parentAudio = "mAudio";
    private static String childPrefix = "mBand";
    private static String meterPrefix = "BandMeter";
    private static int numBands = 0;
    private static int width = 300;
    private static int height = 6;

    public static void main(String[] args)
    {
        try
        {
            //initialize default values
            FileWriter scriptFile = new FileWriter("VUColorometerTest.ini");
            VUScript = new PrintWriter(scriptFile);
            //colors (may change using -C)
            int[] color1 = {77, 49, 198};
            int[] color2 = {247, 27, 225};
            int[] color3 = {255, 0, 157};
            int[] color4 = {242, 38, 93};
            int[][] colors = {color1, color2, color3, color4};

            numBands = 149;


            //print sections
            printHeading();
            VUScript.println("[Variables]");
            printColors(colors);
            printParent();
            printChildren();
            VUScript.println();
            printCalcs();
            VUScript.println();
            printMeters();
            VUScript.println();


            if(args.length > 0)
            {

            }
            VUScript.close();
        }catch(IOException e)
        {
            System.out.println("IO Error: " + e.toString());
        }
    }

    public static void printHeading()
    {
        VUScript.println("[Rainmeter]");
        VUScript.println("Update=25");
        VUScript.println("DynamicWindowSize=1");
        VUScript.println("BackgroundMode=1");
        VUScript.println();
    }

    public static void printColors(int[][] colors) throws IOException
    {
        if(colors.length != 4) throw new IOException("too many colors");
        for(int i=0; i<colors.length; i++)
        {
            int[] color = colors[i];
            if(color.length != 3) throw new IOException("color must be RGB integers");
            VUScript.printf("r%d=%d\ng%d=%d\nb%d=%d\n\n", i+1, color[0], i+1, color[1], i+1, color[2]);
        }
    }

    public static void printParent()
    {
        VUScript.printf("[%s]\n", parentAudio);
        VUScript.println("Measure=Plugin");
        VUScript.println("Plugin=AudioLevel");
        VUScript.println("Port=Output");
        VUScript.println("FFTSize=4096");
        VUScript.println("FFTAttack=5");
        VUScript.println("FFTDecay=300");
        VUScript.println("Bands="+numBands);
        VUScript.println();
    }

    public static void printChildren()
    {
        for(int i=0; i<numBands; i++)
        {
            VUScript.printf("[%s%d]\n", childPrefix, i);
            VUScript.println("Measure=Plugin");
            VUScript.println("Plugin=AudioLevel");
            VUScript.println("Parent="+parentAudio);
            VUScript.println("Type=Band");
            VUScript.println("BandIdx="+i);
            VUScript.println();
        }
    }

    public static void printCalcBody(int index, char colorComp)
    {
        String c1 = "(#"+colorComp+"1#*(1-(3*[mBand"+index+"])+(3(*[mBand"+index+"]**2))-([mBand"+index+"]**3)))";
        String c2 = "(#"+colorComp+"2#*((3*[mBand"+index+"])-(6*([mBand"+index+"]**2))+(3*([mBand"+index+"]**3))))";
        String c3 = "(#"+colorComp+"3#*((3*([mBand"+index+"]**2))-(3*([mBand"+index+"]**3))))";
        String c4 = "(#"+colorComp+"4#*([mBand"+index+"]**3))";

        VUScript.println("Measure=Calc");
        VUScript.println("DynamicVariables=1");
        VUScript.println("Formula=" + c1 + "+" + c2 + "+" + c3 + "+" + c4);
        VUScript.println();
    }

    public static void printCalcs()
    {
        for(int i=0; i<numBands; i++)
        {
            VUScript.printf("[crBand%d]\n", i);
            printCalcBody(i, 'r');
            VUScript.printf("[cgBand%d]\n", i);
            printCalcBody(i, 'g');
            VUScript.printf("[cbBand%d]\n", i);
            printCalcBody(i, 'b');
        }
    }

    public static void printMeters()
    {

        VUScript.printf("[%s%d]\n", meterPrefix, numBands-1);
        VUScript.println("Meter=Shape");
        VUScript.println("X=0");
        VUScript.println("Y=0");
        VUScript.println("DynamicVariables=1");
        VUScript.println("Shape=Rectangle 0,0,"+width+","+height+" | Fill Color ([crBand"+(numBands-1)+"]),([cgBand"+(numBands-1)+"]),([cbBand"+(numBands-1)+"])");
        VUScript.println();
        for(int i=numBands-2; i>=0; i--)
        {
            VUScript.printf("[%s%d]\n", meterPrefix, i);
            VUScript.println("Meter=Shape");
            VUScript.println("X=0r");
            VUScript.println("Y=-1R");
            VUScript.println("DynamicVariables=1");
            VUScript.println("Shape=Rectangle 0,0,"+width+","+height+" | Fill Color ([crBand"+i+"]),([cgBand"+i+"]),([cbBand"+i+"])");
            VUScript.println();
        }
    }
}
