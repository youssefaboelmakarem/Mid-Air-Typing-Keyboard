package com.mx.krakensoft.opencv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.mx.krakensoft.opencv.imageProcessing.ColorBlobDetector;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.core.Size;

public class MainActivity extends Activity implements OnTouchListener, CvCameraViewListener2 {

    static {
        System.loadLibrary("opencv_java3");
    }
    private static final String    TAG                 = "HandPose::MainActivity";
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private Mat                    mRgba;
    private Mat                    mGray;
    private Mat 					mIntermediateMat;

    private int                    mDetectorType       = JAVA_DETECTOR;

    private CustomSufaceView   mOpenCvCameraView;
    private List<Size> mResolutionList;

    private SeekBar minTresholdSeekbar = null;
    private SeekBar maxTresholdSeekbar = null;
    private TextView minTresholdSeekbarText = null;
    private TextView numberOfFingersText = null;

    // Initiating buttons and letters
    private TextView LetterA = null;
    private TextView LetterQ = null;
    private TextView LetterW = null;
    private TextView LetterE = null;
    private TextView LetterR = null;
    private TextView LetterT = null;
    private TextView LetterY = null;
    private TextView LetterU = null;
    private TextView LetterI = null;
    private TextView LetterO = null;
    private TextView LetterP = null;
    private TextView LetterS = null;
    private TextView LetterD = null;
    private TextView LetterF = null;
    private TextView LetterG = null;
    private TextView LetterH = null;
    private TextView LetterJ = null;
    private TextView LetterK = null;
    private TextView LetterL = null;
    private TextView LetterZ = null;
    private TextView LetterX = null;
    private TextView LetterC = null;
    private TextView LetterV = null;
    private TextView LetterB = null;
    private TextView LetterN = null;
    private TextView LetterM = null;
    private TextView Space = null;
    private TextView Backspace = null;
    private TextView Touch = null;
    private TextView MidAir = null;
    private TextView Test = null;

    private int counter = 0;

    // flags for detecting the test mode whether touch mode or mid air mode
    private boolean testFlagTouch = false;
    private boolean testFlagAir = false;


    // Counters for errors, time measuring and sentence number
    private int touchError = 0;
    private int airError = 0;
    private long startTimeTouch;
    private long endTimeTouch;
    private long elapsedTimeTouch;
    private long startTimeAir;
    private long endTimeAir;
    private long elapsedTimeAir;
    private int touchCounter = 1;
    private int midAirCounter = 1;

    //private String mText = "Hello World";
    private final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    // Initiating block number
    private int touchBlock = 1;
    private int midAirBlock = 1;



    private int touchFlag = 0;

    private String letter;
    private TextView textView14 = null;

    double iThreshold = 0;

    private Scalar               	mBlobColorHsv;
    private Scalar               	mBlobColorRgba;
    private ColorBlobDetector    	mDetector;
    private Mat                  	mSpectrum;
    private boolean				mIsColorSelected = false;

    private Size                 	SPECTRUM_SIZE;
    private Scalar               	CONTOUR_COLOR;
    private Scalar               	CONTOUR_COLOR_WHITE;

    final Handler mHandler = new Handler();

    //Handlers for updating button color when tapped and return it back to default color every frame per second

    final Handler mHandlerQ = new Handler();
    final Handler mHandlerW= new Handler();
    final Handler mHandlerE = new Handler();
    final Handler mHandlerR = new Handler();
    final Handler mHandlerT = new Handler();
    final Handler mHandlerY = new Handler();
    final Handler mHandlerU = new Handler();
    final Handler mHandlerI = new Handler();
    final Handler mHandlerO = new Handler();
    final Handler mHandlerP = new Handler();
    final Handler mHandlerA = new Handler();
    final Handler mHandlerS = new Handler();
    final Handler mHandlerD = new Handler();
    final Handler mHandlerF = new Handler();
    final Handler mHandlerG = new Handler();
    final Handler mHandlerH = new Handler();
    final Handler mHandlerJ = new Handler();
    final Handler mHandlerK = new Handler();
    final Handler mHandlerL = new Handler();
    final Handler mHandlerZ = new Handler();
    final Handler mHandlerX = new Handler();
    final Handler mHandlerC = new Handler();
    final Handler mHandlerV = new Handler();
    final Handler mHandlerB = new Handler();
    final Handler mHandlerN = new Handler();
    final Handler mHandlerM = new Handler();
    final Handler mHandlerSpace = new Handler();
    final Handler mHandlerBackSpace = new Handler();
    final Handler mHandlerTouch = new Handler();
    final Handler mHandlerMidAir = new Handler();
    final Handler mHandlerTest = new Handler();



    final Handler wHandlerQ = new Handler();
    final Handler wHandlerW= new Handler();
    final Handler wHandlerE = new Handler();
    final Handler wHandlerR = new Handler();
    final Handler wHandlerT = new Handler();
    final Handler wHandlerY = new Handler();
    final Handler wHandlerU = new Handler();
    final Handler wHandlerI = new Handler();
    final Handler wHandlerO = new Handler();
    final Handler wHandlerP = new Handler();
    final Handler wHandlerA = new Handler();
    final Handler wHandlerS = new Handler();
    final Handler wHandlerD = new Handler();
    final Handler wHandlerF = new Handler();
    final Handler wHandlerG = new Handler();
    final Handler wHandlerH = new Handler();
    final Handler wHandlerJ = new Handler();
    final Handler wHandlerK = new Handler();
    final Handler wHandlerL = new Handler();
    final Handler wHandlerZ = new Handler();
    final Handler wHandlerX = new Handler();
    final Handler wHandlerC = new Handler();
    final Handler wHandlerV = new Handler();
    final Handler wHandlerB = new Handler();
    final Handler wHandlerN = new Handler();
    final Handler wHandlerM = new Handler();
    final Handler wHandlerSpace = new Handler();
    final Handler wHandlerBackSpace = new Handler();
    final Handler wHandlerTouch = new Handler();
    final Handler wHandlerMidAir = new Handler();
    final Handler wHandlerTest = new Handler();




    int numberOfFingers = 0;

    final Runnable mUpdateFingerCountResults = new Runnable() {
        public void run() {
            updateNumberOfFingers();
        }
    };
    final Runnable mLetterQ = new Runnable() {
        @Override
        public void run() {
            letterQ();
        }
    };

    // Handlers calling methods of button color changing

    final Runnable mLetterW = new Runnable() {
        @Override
        public void run() {
            letterW();
        }
    };
    final Runnable mLetterE = new Runnable() {
        @Override
        public void run() {
            letterE();
        }
    };
    final Runnable mLetterR = new Runnable() {
        @Override
        public void run() {
            letterR();
        }
    };
    final Runnable mLetterT = new Runnable() {
        @Override
        public void run() {
            letterT();
        }
    };
    final Runnable mLetterY = new Runnable() {
        @Override
        public void run() {
            letterY();
        }
    };
    final Runnable mLetterU = new Runnable() {
        @Override
        public void run() {
            letterU();
        }
    };
    final Runnable mLetterI = new Runnable() {
        @Override
        public void run() {
            letterI();
        }
    };
    final Runnable mLetterO = new Runnable() {
        @Override
        public void run() {
            letterO();
        }
    };
    final Runnable mLetterP = new Runnable() {
        @Override
        public void run() {
            letterP();
        }
    };
    final Runnable mLetterA = new Runnable() {
        @Override
        public void run() {
            letterA();
        }
    };
    final Runnable mLetterS = new Runnable() {
        @Override
        public void run() {
            letterS();
        }
    };
    final Runnable mLetterD = new Runnable() {
        @Override
        public void run() {
            letterD();
        }
    };
    final Runnable mLetterF = new Runnable() {
        @Override
        public void run() {
            letterF();
        }
    };
    final Runnable mLetterG = new Runnable() {
        @Override
        public void run() {
            letterG();
        }
    };
    final Runnable mLetterH = new Runnable() {
        @Override
        public void run() {
            letterH();
        }
    };
    final Runnable mLetterJ = new Runnable() {
        @Override
        public void run() {
            letterJ();
        }
    };
    final Runnable mLetterK = new Runnable() {
        @Override
        public void run() {
            letterK();
        }
    };
    final Runnable mLetterL = new Runnable() {
        @Override
        public void run() {
            letterL();
        }
    };
    final Runnable mLetterZ = new Runnable() {
        @Override
        public void run() {
            letterZ();
        }
    };
    final Runnable mLetterX = new Runnable() {
        @Override
        public void run() {
            letterX();
        }
    };
    final Runnable mLetterC = new Runnable() {
        @Override
        public void run() {
            letterC();
        }
    };
    final Runnable mLetterV = new Runnable() {
        @Override
        public void run() {
            letterV();
        }
    };
    final Runnable mLetterB = new Runnable() {
        @Override
        public void run() {
            letterB();
        }
    };
    final Runnable mLetterN = new Runnable() {
        @Override
        public void run() {
            letterN();
        }
    };
    final Runnable mLetterM = new Runnable() {
        @Override
        public void run() {
            letterM();
        }
    };
    final Runnable mSpace = new Runnable() {
        @Override
        public void run() {
            Space();
        }
    };
    final Runnable mBackSpace = new Runnable() {
        @Override
        public void run() {
            backSpace();
        }
    };
    final Runnable mTouch = new Runnable() {
        @Override
        public void run() {
            touch();
        }
    };
    final Runnable mMidAir = new Runnable() {
        @Override
        public void run() {
            midAir();
        }
    };
    final Runnable mTest = new Runnable() {
        @Override
        public void run() {
            test();
        }
    };

    final Runnable wLetterQ = new Runnable() {
        @Override
        public void run() {
            wletterQ();
        }
    };
    final Runnable wLetterW = new Runnable() {
        @Override
        public void run() {
            wletterW();
        }
    };
    final Runnable wLetterE = new Runnable() {
        @Override
        public void run() {
            wletterE();
        }
    };
    final Runnable wLetterR = new Runnable() {
        @Override
        public void run() {
            wletterR();
        }
    };
    final Runnable wLetterT = new Runnable() {
        @Override
        public void run() {
            wletterT();
        }
    };
    final Runnable wLetterY = new Runnable() {
        @Override
        public void run() {
            wletterY();
        }
    };
    final Runnable wLetterU = new Runnable() {
        @Override
        public void run() {
            wletterU();
        }
    };
    final Runnable wLetterI = new Runnable() {
        @Override
        public void run() {
            wletterI();
        }
    };
    final Runnable wLetterO = new Runnable() {
        @Override
        public void run() {
            wletterO();
        }
    };
    final Runnable wLetterP = new Runnable() {
        @Override
        public void run() {
            wletterP();
        }
    };
    final Runnable wLetterA = new Runnable() {
        @Override
        public void run() {
            wletterA();
        }
    };
    final Runnable wLetterS = new Runnable() {
        @Override
        public void run() {
            wletterS();
        }
    };
    final Runnable wLetterD = new Runnable() {
        @Override
        public void run() {
            wletterD();
        }
    };
    final Runnable wLetterF = new Runnable() {
        @Override
        public void run() {
            wletterF();
        }
    };
    final Runnable wLetterG = new Runnable() {
        @Override
        public void run() {
            wletterG();
        }
    };
    final Runnable wLetterH = new Runnable() {
        @Override
        public void run() {
            wletterH();
        }
    };
    final Runnable wLetterJ = new Runnable() {
        @Override
        public void run() {
            wletterJ();
        }
    };
    final Runnable wLetterK = new Runnable() {
        @Override
        public void run() {
            wletterK();
        }
    };
    final Runnable wLetterL = new Runnable() {
        @Override
        public void run() {
            wletterL();
        }
    };
    final Runnable wLetterZ = new Runnable() {
        @Override
        public void run() {
            wletterZ();
        }
    };
    final Runnable wLetterX = new Runnable() {
        @Override
        public void run() {
            wletterX();
        }
    };
    final Runnable wLetterC = new Runnable() {
        @Override
        public void run() {
            wletterC();
        }
    };
    final Runnable wLetterV = new Runnable() {
        @Override
        public void run() {
            wletterV();
        }
    };
    final Runnable wLetterB = new Runnable() {
        @Override
        public void run() {
            wletterB();
        }
    };
    final Runnable wLetterN = new Runnable() {
        @Override
        public void run() {
            wletterN();
        }
    };
    final Runnable wLetterM = new Runnable() {
        @Override
        public void run() {
            wletterM();
        }
    };
    final Runnable wSpace = new Runnable() {
        @Override
        public void run() {
            wSpace();
        }
    };
    final Runnable wBackSpace = new Runnable() {
        @Override
        public void run() {
            wBackSpace();
        }
    };
    final Runnable wTouch = new Runnable() {
        @Override
        public void run() {
            wTouch();
        }
    };
    final Runnable wMidAir = new Runnable() {
        @Override
        public void run() {
            wMidAir();
        }
    };
    final Runnable wTest = new Runnable() {
        @Override
        public void run() {
            wTest();
        }
    };

    MyInputMethodService myInputMethodService = new MyInputMethodService();

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {            // Loading OpenCV
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    //Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                    // 640x480
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() throws FileNotFoundException {
        //Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "called onCreate");

        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main_surface_view);
        if (!OpenCVLoader.initDebug()) {
            Log.e("Test","man");
        }else{
        }

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        StringBuilder text = new StringBuilder();
        dir.mkdirs();
        String filename = "TouchTest.txt";
        File file = new File(dir, filename);

        mOpenCvCameraView = (CustomSufaceView) findViewById(R.id.main_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        minTresholdSeekbarText = (TextView) findViewById(R.id.textView3);


        numberOfFingersText = (TextView) findViewById(R.id.numberOfFingers);
        textView14 = (TextView) findViewById(R.id.textView14);

        // Setting buttons layouts by x-y coordnates

        LetterQ = (TextView) findViewById(R.id.LetterQ);
        LetterQ.setX(400-2215);
        LetterQ.setY(200);

        LetterW = (TextView) findViewById(R.id.LetterW);
        LetterW.setX(550-2215);
        LetterW.setY(200);

        LetterE = (TextView) findViewById(R.id.LetterE);
        LetterE.setX(700-2215);
        LetterE.setY(200);

        LetterR = (TextView) findViewById(R.id.LetterR);
        LetterR.setX(850-2215);
        LetterR.setY(200);

        LetterT = (TextView) findViewById(R.id.LetterT);
        LetterT.setX(1000-2215);
        LetterT.setY(200);

        LetterY = (TextView) findViewById(R.id.LetterY);
        LetterY.setX(1150-2215);
        LetterY.setY(200);

        LetterU = (TextView) findViewById(R.id.LetterU);
        LetterU.setX(1300-2215);
        LetterU.setY(200);

        LetterI = (TextView) findViewById(R.id.LetterI);
        LetterI.setX(1450-2215);
        LetterI.setY(200);

        LetterO = (TextView) findViewById(R.id.LetterO);
        LetterO.setX(1600-2215);
        LetterO.setY(200);

        LetterP = (TextView) findViewById(R.id.LetterP);
        LetterP.setX(1750-2215);
        LetterP.setY(200);

        LetterA = (TextView) findViewById(R.id.LetterA);
        LetterA.setX(500-2215);
        LetterA.setY(400);

        LetterS = (TextView) findViewById(R.id.LetterS);
        LetterS.setX(650-2215);
        LetterS.setY(400);

        LetterD = (TextView) findViewById(R.id.LetterD);
        LetterD.setX(800-2215);
        LetterD.setY(400);

        LetterF = (TextView) findViewById(R.id.LetterF);
        LetterF.setX(950-2215);
        LetterF.setY(400);

        LetterG = (TextView) findViewById(R.id.LetterG);
        LetterG.setX(1100-2215);
        LetterG.setY(400);

        LetterH = (TextView) findViewById(R.id.LetterH);
        LetterH.setX(1250-2215);
        LetterH.setY(400);

        LetterJ = (TextView) findViewById(R.id.LetterJ);
        LetterJ.setX(1400-2215);
        LetterJ.setY(400);

        LetterK = (TextView) findViewById(R.id.LetterK);
        LetterK.setX(1550-2215);
        LetterK.setY(400);

        LetterL = (TextView) findViewById(R.id.LetterL);
        LetterL.setX(1700-2215);
        LetterL.setY(400);

        Backspace = (TextView) findViewById(R.id.Backspace);
        Backspace.setX(1925-2215);
        Backspace.setY(400);

        LetterZ = (TextView) findViewById(R.id.LetterZ);
        LetterZ.setX(550-2215);
        LetterZ.setY(600);

        LetterX = (TextView) findViewById(R.id.LetterX);
        LetterX.setX(700-2215);
        LetterX.setY(600);

        LetterC = (TextView) findViewById(R.id.LetterC);
        LetterC.setX(850-2215);
        LetterC.setY(600);

        LetterV = (TextView) findViewById(R.id.LetterV);
        LetterV.setX(1000-2215);
        LetterV.setY(600);

        LetterB = (TextView) findViewById(R.id.LetterB);
        LetterB.setX(1150-2215);
        LetterB.setY(600);

        LetterN = (TextView) findViewById(R.id.LetterN);
        LetterN.setX(1300-2215);
        LetterN.setY(600);

        LetterM = (TextView) findViewById(R.id.LetterM);
        LetterM.setX(1450-2215);
        LetterM.setY(600);

        Space = (TextView) findViewById(R.id.Space);
        Space.setX(1725-2215);
        Space.setY(600);

        Touch = (TextView) findViewById(R.id.Touch);
        Touch.setX(325-2215);
        Touch.setY(400);

        MidAir = (TextView) findViewById(R.id.MidAir);
        MidAir.setX(340-2215);
        MidAir.setY(550);

        Test = (TextView) findViewById(R.id.Test);
        Test.setX(1925-2215);
        Test.setY(600);



        minTresholdSeekbar = (SeekBar)findViewById(R.id.seekBar1);
        minTresholdSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){ //Threshold bar
                progressChanged = progress;
                minTresholdSeekbarText.setText(String.valueOf(progressChanged));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                minTresholdSeekbarText.setText(String.valueOf(progressChanged));
            }
        });
        minTresholdSeekbar.setProgress(8600);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        mIntermediateMat = new Mat();


        /*
        mResolutionList = mOpenCvCameraView.getResolutionList();
        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            Log.i(TAG, "Resolution Option ["+Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString()+"]");
        }

        Size resolution = mResolutionList.get(7);
        mOpenCvCameraView.setResolution(resolution);
        resolution = mOpenCvCameraView.getResolution();
        String caption = "Resolution "+ Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
        Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        */
        Camera.Size resolution = mOpenCvCameraView.getResolution();
        String caption = "Resolution "+ Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
        Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();

        Camera.Parameters cParams = mOpenCvCameraView.getParameters();
        cParams.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        mOpenCvCameraView.setParameters(cParams);
        Toast.makeText(this, "Focus mode : "+cParams.getFocusMode(), Toast.LENGTH_SHORT).show();

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,0,255);
        CONTOUR_COLOR_WHITE = new Scalar(255,255,255,255);

    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();  // Setting touch coordinates for the Touch mode by calculating position related to screen
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset - 100;
        int y = (int)event.getY() - yOffset + 130;

        /*Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");
        int qx = (int) (LetterQ.getX() - 150);
        int qy = (int) (LetterQ.getY() + 200);
        Log.i(TAG, "Q : (" + qx + ", " + qy + ")");*/

        /*if (textView14.getText() == null) {
            textView14.setText("Q");
        } else {
            textView14.setText(textView14.getText() + "Q");
        }*/
        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        // Switching between Touch mode and Mid Air mode
        // If the touch button is selected, touch test mode will be on
        if (x > Touch.getX() - 150 && x < Touch.getX() - 150 + 100 && y > Touch.getY() + 100 && y < Touch.getY() + 200 + 100) {
            mHandlerTouch.post(mTouch);
            touchFlag = 1;

            //Log.d(TAG, "Letter " + textView14.getX());
        }

        // If the mid air button is selected, mid air test mode will be on

        if (x > MidAir.getX() - 150 && x < MidAir.getX() - 150 + 100 && y > MidAir.getY() + 100 && y < MidAir.getY() + 200 + 100) {
            mHandlerMidAir.post(mMidAir);
            touchFlag = 0;
            //Log.d(TAG, "Letter " + textView14.getX());
        }




        if (touchFlag == 1) { // Touch mode
            if(testFlagTouch == true){ // Touch Test mode
                // Letters tapping and writing according to coordinates of finger touch and coordinates of letters, as if coordinates of touch tap
                // matches coordinates of a letter, this letter will be typed
                if (x > LetterQ.getX() - 150 - 75 && x < LetterQ.getX() - 150 + 75 && y > LetterQ.getY() + 200 - 75 && y < LetterQ.getY() + 200 + 75) {
                    mHandlerQ.post(mLetterQ); // Handler flicks button color from white to red when tapped
                    if (textView14.getText() == null) { // If text view is empty this will set the text view to "Q"
                        textView14.setText("Q");
                       // myInputMethodService.press("Q");
                    } else {
                        textView14.setText(textView14.getText() + "Q"); // If text view is not empty, this will append the letter "Q" to it
                        //myInputMethodService.press("Q");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterW.getX() - 150 - 75 && x < LetterW.getX() - 150 + 75 && y > LetterW.getY() + 200 - 75 && y < LetterW.getY() + 200 + 75) {
                    mHandlerW.post(mLetterW);
                    if (textView14.getText() == null) {
                        textView14.setText("W");
                    } else {
                        textView14.setText(textView14.getText() + "W");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterE.getX() - 150 - 75 && x < LetterE.getX() - 150 + 75 && y > LetterE.getY() + 200 - 75 && y < LetterE.getY() + 200 + 75) {
                    mHandlerE.post(mLetterE);
                    if (textView14.getText() == null) {
                        textView14.setText("E");
                    } else {
                        textView14.setText(textView14.getText() + "E");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterR.getX() - 150 - 75 && x < LetterR.getX() - 150 + 75 && y > LetterR.getY() + 200 - 75 && y < LetterR.getY() + 200 + 75) {
                    mHandlerR.post(mLetterR);
                    if (textView14.getText() == null) {
                        textView14.setText("R");
                    } else {
                        textView14.setText(textView14.getText() + "R");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterT.getX() - 150 - 75 && x < LetterT.getX() - 150 + 75 && y > LetterT.getY() + 200 - 75 && y < LetterT.getY() + 200 + 75) {
                    mHandlerT.post(mLetterT);
                    if (textView14.getText() == null) {
                        textView14.setText("T");
                    } else {
                        textView14.setText(textView14.getText() + "T");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterY.getX() - 150 - 75 && x < LetterY.getX() - 150 + 75 && y > LetterY.getY() + 200 - 75 && y < LetterY.getY() + 200 + 75) {
                    mHandlerY.post(mLetterY);
                    if (textView14.getText() == null) {
                        textView14.setText("Y");
                    } else {
                        textView14.setText(textView14.getText() + "Y");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterU.getX() - 150 - 75 && x < LetterU.getX() - 150 + 75 && y > LetterU.getY() + 200 - 75 && y < LetterU.getY() + 200 + 75) {
                    mHandlerU.post(mLetterU);
                    if (textView14.getText() == null) {
                        textView14.setText("U");
                    } else {
                        textView14.setText(textView14.getText() + "U");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterI.getX() - 150 - 75 && x < LetterI.getX() - 150 + 75 && y > LetterI.getY() + 200 - 75 && y < LetterI.getY() + 200 + 75) {
                    mHandlerI.post(mLetterI);
                    if (textView14.getText() == null) {
                        textView14.setText("I");
                    } else {
                        textView14.setText(textView14.getText() + "I");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterO.getX() - 150 - 75 && x < LetterO.getX() - 150 + 75 && y > LetterO.getY() + 200 - 75 && y < LetterO.getY() + 200 + 75) {
                    mHandlerO.post(mLetterO);
                    if (textView14.getText() == null) {
                        textView14.setText("O");
                    } else {
                        textView14.setText(textView14.getText() + "O");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterP.getX() - 150 - 75 && x < LetterP.getX() - 150 + 75 && y > LetterP.getY() + 200 - 75 && y < LetterP.getY() + 200 + 75) {
                    mHandlerP.post(mLetterP);
                    if (textView14.getText() == null) {
                        textView14.setText("P");
                    } else {
                        textView14.setText(textView14.getText() + "P");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterA.getX() - 150 - 75 && x < LetterA.getX() - 150 + 75 && y > LetterA.getY() + 200 - 75 && y < LetterA.getY() + 200 + 75) {
                    mHandlerA.post(mLetterA);
                    if (textView14.getText() == null) {
                        textView14.setText("A");
                    } else {
                        textView14.setText(textView14.getText() + "A");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterS.getX() - 150 - 75 && x < LetterS.getX() - 150 + 75 && y > LetterS.getY() + 200 - 75 && y < LetterS.getY() + 200 + 75) {
                    mHandlerS.post(mLetterS);
                    if (textView14.getText() == null) {
                        textView14.setText("S");
                    } else {
                        textView14.setText(textView14.getText() + "S");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterD.getX() - 150 - 75 && x < LetterD.getX() - 150 + 75 && y > LetterD.getY() + 200 - 75 && y < LetterD.getY() + 200 + 75) {
                    mHandlerD.post(mLetterD);
                    if (textView14.getText() == null) {
                        textView14.setText("D");
                    } else {
                        textView14.setText(textView14.getText() + "D");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterF.getX() - 150 - 75 && x < LetterF.getX() - 150 + 75 && y > LetterF.getY() + 200 - 75 && y < LetterF.getY() + 200 + 75) {
                    mHandlerF.post(mLetterF);
                    if (textView14.getText() == null) {
                        textView14.setText("F");
                    } else {
                        textView14.setText(textView14.getText() + "F");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterG.getX() - 150 - 75 && x < LetterG.getX() - 150 + 75 && y > LetterG.getY() + 200 - 75 && y < LetterG.getY() + 200 + 75) {
                    mHandlerG.post(mLetterG);
                    if (textView14.getText() == null) {
                        textView14.setText("G");
                    } else {
                        textView14.setText(textView14.getText() + "G");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterH.getX() - 150 - 75 && x < LetterH.getX() - 150 + 75 && y > LetterH.getY() + 200 - 75 && y < LetterH.getY() + 200 + 75) {
                    mHandlerH.post(mLetterH);
                    if (textView14.getText() == null) {
                        textView14.setText("H");
                    } else {
                        textView14.setText(textView14.getText() + "H");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterJ.getX() - 150 - 75 && x < LetterJ.getX() - 150 + 75 && y > LetterJ.getY() + 200 - 75 && y < LetterJ.getY() + 200 + 75) {
                    mHandlerJ.post(mLetterJ);
                    if (textView14.getText() == null) {
                        textView14.setText("J");
                    } else {
                        textView14.setText(textView14.getText() + "J");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterK.getX() - 150 - 75 && x < LetterK.getX() - 150 + 75 && y > LetterK.getY() + 200 - 75 && y < LetterK.getY() + 200 + 75) {
                    mHandlerK.post(mLetterK);
                    if (textView14.getText() == null) {
                        textView14.setText("K");
                    } else {
                        textView14.setText(textView14.getText() + "K");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterL.getX() - 150 - 75 && x < LetterL.getX() - 150 + 75 && y > LetterL.getY() + 200 - 75 && y < LetterL.getY() + 200 + 75) {
                    mHandlerL.post(mLetterL);
                    if (textView14.getText() == null) {
                        textView14.setText("L");
                    } else {
                        textView14.setText(textView14.getText() + "L");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterZ.getX() - 150 - 75 && x < LetterZ.getX() - 150 + 75 && y > LetterZ.getY() + 200 - 75 && y < LetterZ.getY() + 200 + 75) {
                    mHandlerZ.post(mLetterZ);
                    if (textView14.getText() == null) {
                        textView14.setText("Z");
                    } else {
                        textView14.setText(textView14.getText() + "Z");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterX.getX() - 150 - 75 && x < LetterX.getX() - 150 + 75 && y > LetterX.getY() + 200 - 75 && y < LetterX.getY() + 200 + 75) {
                    mHandlerX.post(mLetterX);
                    if (textView14.getText() == null) {
                        textView14.setText("X");
                    } else {
                        textView14.setText(textView14.getText() + "X");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterC.getX() - 150 - 75 && x < LetterC.getX() - 150 + 75 && y > LetterC.getY() + 200 - 75 && y < LetterC.getY() + 200 + 75) {
                    mHandlerC.post(mLetterC);
                    if (textView14.getText() == null) {
                        textView14.setText("C");
                    } else {
                        textView14.setText(textView14.getText() + "C");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterV.getX() - 150 - 75 && x < LetterV.getX() - 150 + 75 && y > LetterV.getY() + 200 - 75 && y < LetterV.getY() + 200 + 75) {
                    mHandlerV.post(mLetterV);
                    if (textView14.getText() == null) {
                        textView14.setText("V");
                    } else {
                        textView14.setText(textView14.getText() + "V");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterB.getX() - 150 - 75 && x < LetterB.getX() - 150 + 75 && y > LetterB.getY() + 200 - 75 && y < LetterB.getY() + 200 + 75) {
                    mHandlerB.post(mLetterB);
                    if (textView14.getText() == null) {
                        textView14.setText("B");
                    } else {
                        textView14.setText(textView14.getText() + "B");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterN.getX() - 150 - 75 && x < LetterN.getX() - 150 + 75 && y > LetterN.getY() + 200 - 75 && y < LetterN.getY() + 200 + 75) {
                    mHandlerN.post(mLetterN);
                    if (textView14.getText() == null) {
                        textView14.setText("N");
                    } else {
                        textView14.setText(textView14.getText() + "N");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterM.getX() - 150 - 75 && x < LetterM.getX() - 150 + 75 && y > LetterM.getY() + 200 - 75 && y < LetterM.getY() + 200 + 75) {
                    mHandlerM.post(mLetterM);
                    if (textView14.getText() == null) {
                        textView14.setText("M");

                    } else {
                        textView14.setText(textView14.getText() + "M");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > Space.getX() - 150 - 75 && x < Space.getX() - 150 + 150 && y > Space.getY() + 200 - 75 && y < Space.getY() + 200 + 75) {
                    mHandlerSpace.post(mSpace);
                    if (textView14.getText() == null) {
                        textView14.setText(" ");
                    } else {
                        textView14.setText(textView14.getText() + " ");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }


                if (x > Backspace.getX() - 150 && x < Backspace.getX() - 150 + 100 && y > Backspace.getY() + 200 && y < Backspace.getY() + 200 + 100) {
                    mHandlerBackSpace.post(mBackSpace);
                    touchError++;
                    if (textView14.length() > 0) {
                        textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                    } else {
                        textView14.setText("");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }

                // Switching between test blocks where each block has 15 different sentences

                if (x > Test.getX() - 150 && x < Test.getX() - 150 + 100 && y > Test.getY() + 100 && y < Test.getY() + 200 + 100) {
                    testFlagTouch = true;
                    mHandlerTest.post(mTest);
                    if (touchBlock == 1) {
                        touchBlock1();
                    }
                    if (touchBlock == 2){
                        touchBlock2();
                    }
                    if (touchBlock == 3){
                        touchBlock3();
                    }
                    if (touchBlock == 4){
                        touchBlock4();
                    }
                    if(touchBlock == 5){
                        touchBlock5();
                    }
                    if (touchBlock == 6){
                        touchBlock6();
                    }

                }
                endTimeTouch = System.nanoTime();
                elapsedTimeTouch = elapsedTimeTouch + (endTimeTouch - startTimeTouch) / 1000000000; // Measuring time of each sentence written
            }
            else { // Writing in touch mode without test mode
                if (x > LetterQ.getX() - 150 - 75 && x < LetterQ.getX() - 150 + 75 && y > LetterQ.getY() + 200 - 75 && y < LetterQ.getY() + 200 + 75) {
                    mHandlerQ.post(mLetterQ);
                    if (textView14.getText() == null) {
                        textView14.setText("Q");
                       // myInputMethodService.press("Q");
                    } else {
                        textView14.setText(textView14.getText() + "Q");
                        //myInputMethodService.press("Q");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterW.getX() - 150 - 75 && x < LetterW.getX() - 150 + 75 && y > LetterW.getY() + 200 - 75 && y < LetterW.getY() + 200 + 75) {
                    mHandlerW.post(mLetterW);
                    if (textView14.getText() == null) {
                        textView14.setText("W");
                    } else {
                        textView14.setText(textView14.getText() + "W");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterE.getX() - 150 - 75 && x < LetterE.getX() - 150 + 75 && y > LetterE.getY() + 200 - 75 && y < LetterE.getY() + 200 + 75) {
                    mHandlerE.post(mLetterE);
                    if (textView14.getText() == null) {
                        textView14.setText("E");
                    } else {
                        textView14.setText(textView14.getText() + "E");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterR.getX() - 150 - 75 && x < LetterR.getX() - 150 + 75 && y > LetterR.getY() + 200 - 75 && y < LetterR.getY() + 200 + 75) {
                    mHandlerR.post(mLetterR);
                    if (textView14.getText() == null) {
                        textView14.setText("R");
                    } else {
                        textView14.setText(textView14.getText() + "R");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterT.getX() - 150 - 75 && x < LetterT.getX() - 150 + 75 && y > LetterT.getY() + 200 - 75 && y < LetterT.getY() + 200 + 75) {
                    mHandlerT.post(mLetterT);
                    if (textView14.getText() == null) {
                        textView14.setText("T");
                    } else {
                        textView14.setText(textView14.getText() + "T");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterY.getX() - 150 - 75 && x < LetterY.getX() - 150 + 75 && y > LetterY.getY() + 200 - 75 && y < LetterY.getY() + 200 + 75) {
                    mHandlerY.post(mLetterY);
                    if (textView14.getText() == null) {
                        textView14.setText("Y");
                    } else {
                        textView14.setText(textView14.getText() + "Y");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterU.getX() - 150 - 75 && x < LetterU.getX() - 150 + 75 && y > LetterU.getY() + 200 - 75 && y < LetterU.getY() + 200 + 75) {
                    mHandlerU.post(mLetterU);
                    if (textView14.getText() == null) {
                        textView14.setText("U");
                    } else {
                        textView14.setText(textView14.getText() + "U");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterI.getX() - 150 - 75 && x < LetterI.getX() - 150 + 75 && y > LetterI.getY() + 200 - 75 && y < LetterI.getY() + 200 + 75) {
                    mHandlerI.post(mLetterI);
                    if (textView14.getText() == null) {
                        textView14.setText("I");
                    } else {
                        textView14.setText(textView14.getText() + "I");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterO.getX() - 150 - 75 && x < LetterO.getX() - 150 + 75 && y > LetterO.getY() + 200 - 75 && y < LetterO.getY() + 200 + 75) {
                    mHandlerO.post(mLetterO);
                    if (textView14.getText() == null) {
                        textView14.setText("O");
                    } else {
                        textView14.setText(textView14.getText() + "O");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterP.getX() - 150 - 75 && x < LetterP.getX() - 150 + 75 && y > LetterP.getY() + 200 - 75 && y < LetterP.getY() + 200 + 75) {
                    mHandlerP.post(mLetterP);
                    if (textView14.getText() == null) {
                        textView14.setText("P");
                    } else {
                        textView14.setText(textView14.getText() + "P");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterA.getX() - 150 - 75 && x < LetterA.getX() - 150 + 75 && y > LetterA.getY() + 200 - 75 && y < LetterA.getY() + 200 + 75) {
                    mHandlerA.post(mLetterA);
                    if (textView14.getText() == null) {
                        textView14.setText("A");
                    } else {
                        textView14.setText(textView14.getText() + "A");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterS.getX() - 150 - 75 && x < LetterS.getX() - 150 + 75 && y > LetterS.getY() + 200 - 75 && y < LetterS.getY() + 200 + 75) {
                    mHandlerS.post(mLetterS);
                    if (textView14.getText() == null) {
                        textView14.setText("S");
                    } else {
                        textView14.setText(textView14.getText() + "S");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterD.getX() - 150 - 75 && x < LetterD.getX() - 150 + 75 && y > LetterD.getY() + 200 - 75 && y < LetterD.getY() + 200 + 75) {
                    mHandlerD.post(mLetterD);
                    if (textView14.getText() == null) {
                        textView14.setText("D");
                    } else {
                        textView14.setText(textView14.getText() + "D");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterF.getX() - 150 - 75 && x < LetterF.getX() - 150 + 75 && y > LetterF.getY() + 200 - 75 && y < LetterF.getY() + 200 + 75) {
                    mHandlerF.post(mLetterF);
                    if (textView14.getText() == null) {
                        textView14.setText("F");
                    } else {
                        textView14.setText(textView14.getText() + "F");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterG.getX() - 150 - 75 && x < LetterG.getX() - 150 + 75 && y > LetterG.getY() + 200 - 75 && y < LetterG.getY() + 200 + 75) {
                    mHandlerG.post(mLetterG);
                    if (textView14.getText() == null) {
                        textView14.setText("G");
                    } else {
                        textView14.setText(textView14.getText() + "G");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterH.getX() - 150 - 75 && x < LetterH.getX() - 150 + 75 && y > LetterH.getY() + 200 - 75 && y < LetterH.getY() + 200 + 75) {
                    mHandlerH.post(mLetterH);
                    if (textView14.getText() == null) {
                        textView14.setText("H");
                    } else {
                        textView14.setText(textView14.getText() + "H");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterJ.getX() - 150 - 75 && x < LetterJ.getX() - 150 + 75 && y > LetterJ.getY() + 200 - 75 && y < LetterJ.getY() + 200 + 75) {
                    mHandlerJ.post(mLetterJ);
                    if (textView14.getText() == null) {
                        textView14.setText("J");
                    } else {
                        textView14.setText(textView14.getText() + "J");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterK.getX() - 150 - 75 && x < LetterK.getX() - 150 + 75 && y > LetterK.getY() + 200 - 75 && y < LetterK.getY() + 200 + 75) {
                    mHandlerK.post(mLetterK);
                    if (textView14.getText() == null) {
                        textView14.setText("K");
                    } else {
                        textView14.setText(textView14.getText() + "K");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterL.getX() - 150 - 75 && x < LetterL.getX() - 150 + 75 && y > LetterL.getY() + 200 - 75 && y < LetterL.getY() + 200 + 75) {
                    mHandlerL.post(mLetterL);
                    if (textView14.getText() == null) {
                        textView14.setText("L");
                    } else {
                        textView14.setText(textView14.getText() + "L");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterZ.getX() - 150 - 75 && x < LetterZ.getX() - 150 + 75 && y > LetterZ.getY() + 200 - 75 && y < LetterZ.getY() + 200 + 75) {
                    mHandlerZ.post(mLetterZ);
                    if (textView14.getText() == null) {
                        textView14.setText("Z");
                    } else {
                        textView14.setText(textView14.getText() + "Z");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterX.getX() - 150 - 75 && x < LetterX.getX() - 150 + 75 && y > LetterX.getY() + 200 - 75 && y < LetterX.getY() + 200 + 75) {
                    mHandlerX.post(mLetterX);
                    if (textView14.getText() == null) {
                        textView14.setText("X");
                    } else {
                        textView14.setText(textView14.getText() + "X");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterC.getX() - 150 - 75 && x < LetterC.getX() - 150 + 75 && y > LetterC.getY() + 200 - 75 && y < LetterC.getY() + 200 + 75) {
                    mHandlerC.post(mLetterC);
                    if (textView14.getText() == null) {
                        textView14.setText("C");
                    } else {
                        textView14.setText(textView14.getText() + "C");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterV.getX() - 150 - 75 && x < LetterV.getX() - 150 + 75 && y > LetterV.getY() + 200 - 75 && y < LetterV.getY() + 200 + 75) {
                    mHandlerV.post(mLetterV);
                    if (textView14.getText() == null) {
                        textView14.setText("V");
                    } else {
                        textView14.setText(textView14.getText() + "V");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterB.getX() - 150 - 75 && x < LetterB.getX() - 150 + 75 && y > LetterB.getY() + 200 - 75 && y < LetterB.getY() + 200 + 75) {
                    mHandlerB.post(mLetterB);
                    if (textView14.getText() == null) {
                        textView14.setText("B");
                    } else {
                        textView14.setText(textView14.getText() + "B");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterN.getX() - 150 - 75 && x < LetterN.getX() - 150 + 75 && y > LetterN.getY() + 200 - 75 && y < LetterN.getY() + 200 + 75) {
                    mHandlerN.post(mLetterN);
                    if (textView14.getText() == null) {
                        textView14.setText("N");
                    } else {
                        textView14.setText(textView14.getText() + "N");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > LetterM.getX() - 150 - 75 && x < LetterM.getX() - 150 + 75 && y > LetterM.getY() + 200 - 75 && y < LetterM.getY() + 200 + 75) {
                    mHandlerM.post(mLetterM);
                    if (textView14.getText() == null) {
                        textView14.setText("M");

                    } else {
                        textView14.setText(textView14.getText() + "M");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }
                if (x > Space.getX() - 150 - 75 && x < Space.getX() - 150 + 150 && y > Space.getY() + 200 - 75 && y < Space.getY() + 200 + 75) {
                    mHandlerSpace.post(mSpace);
                    if (textView14.getText() == null) {
                        textView14.setText(" ");
                    } else {
                        textView14.setText(textView14.getText() + " ");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }


                if (x > Backspace.getX() - 150 && x < Backspace.getX() - 150 + 100 && y > Backspace.getY() + 200 && y < Backspace.getY() + 200 + 100) {
                    mHandlerBackSpace.post(mBackSpace);
                    if (textView14.length() > 0) {
                        textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                    } else {
                        textView14.setText("");
                    }
                    //Log.d(TAG, "Letter " + textView14.getX());
                }


                if (x > Test.getX() - 150 && x < Test.getX() - 150 + 100 && y > Test.getY() + 100 && y < Test.getY() + 200 + 100) { // Starting touch test mode
                    testFlagTouch = true;
                    mHandlerTest.post(mTest);
                    textView14.setText("BLOCK 1");
                }
            }


        }



        //Toast.makeText(this, filename + " is saved to \n" + dir, Toast.LENGTH_SHORT).show();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
            }
            else{
                saveToTxtFile(mText);
            }
        }
        else {
            saveToTxtFile(mText);
        }*/



        //Log.d(TAG, "Time Elapsed: " + elapsedTimeTouch);



        Rect touchedRect = new Rect();

        touchedRect.x = (x>5) ? x-5 : 0;
        touchedRect.y = (y>5) ? y-5 : 0;

        touchedRect.width = (x+5 < cols) ? x + 5 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+5 < rows) ? y + 5 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        /*Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");*/

        mDetector.setHsvColor(mBlobColorHsv);

        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    saveToTxtFile(mText);

                }
                else {
                    Toast.makeText(this, "Storage Permission is required to store", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

   /* private void saveToTxtFile(String mText) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        try{
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/Bachelor/");
            dir.mkdirs();
            String filename = "MyFile_" + "TEST" + ".txt";
            File file = new File(dir, filename);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mText);
            bw.close();

            Toast.makeText(this, filename + " is saved to \n" + dir, Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }



    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Core.flip(mRgba,mRgba,-1); //Flipping orientation to adopt with right hand
        Core.flip(mGray,mGray,-1);
        iThreshold = minTresholdSeekbar.getProgress();



        //Imgproc.blur(mRgba, mRgba, new Size(5,5));
        Imgproc.GaussianBlur(mRgba, mRgba, new org.opencv.core.Size(3, 3), 1, 1);
        //Imgproc.medianBlur(mRgba, mRgba, 3);

        if (!mIsColorSelected) return mRgba;

        List<MatOfPoint> contours = mDetector.getContours();
        mDetector.process(mRgba);

        //Log.d(TAG, "Contours count: " + contours.size());

        if (contours.size() <= 0) {
            return mRgba;
        }

        RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(0)	.toArray()));

        double boundWidth = rect.size.width;
        double boundHeight = rect.size.height;
        int boundPos = 0;

        for (int i = 1; i < contours.size(); i++) {
            rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            if (rect.size.width * rect.size.height > boundWidth * boundHeight) {
                boundWidth = rect.size.width;
                boundHeight = rect.size.height;
                boundPos = i;
            }
        }

        Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(boundPos).toArray()));

        Imgproc.rectangle( mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR_WHITE, 2, 8, 0 );


        /*Log.d(TAG,
                " Row start ["+
                        (int) boundRect.tl().y + "] row end ["+
                        (int) boundRect.br().y+"] Col start ["+
                        (int) boundRect.tl().x+"] Col end ["+
                        (int) boundRect.br().x+"]");*/

        int rectHeightThresh = 0;
        double a = boundRect.br().y - boundRect.tl().y;
        a = a * 0.7;
        a = boundRect.tl().y + a;

        /*Log.d(TAG,
                " A ["+a+"] br y - tl y = ["+(boundRect.br().y - boundRect.tl().y)+"]");*/

        //Core.rectangle( mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR, 2, 8, 0 );
        Imgproc.rectangle( mRgba, boundRect.tl(), new Point(boundRect.br().x, a), CONTOUR_COLOR, 2, 8, 0 );

        MatOfPoint2f pointMat = new MatOfPoint2f();
        Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(boundPos).toArray()), pointMat, 3, true);
        contours.set(boundPos, new MatOfPoint(pointMat.toArray()));

        MatOfInt hull = new MatOfInt();
        MatOfInt4 convexDefect = new MatOfInt4();
        Imgproc.convexHull(new MatOfPoint(contours.get(boundPos).toArray()), hull);

        if(hull.toArray().length < 3) return mRgba;

        Imgproc.convexityDefects(new MatOfPoint(contours.get(boundPos)	.toArray()), hull, convexDefect);

        List<MatOfPoint> hullPoints = new LinkedList<MatOfPoint>();
        List<Point> listPo = new LinkedList<Point>();
        for (int j = 0; j < hull.toList().size(); j++) {
            listPo.add(contours.get(boundPos).toList().get(hull.toList().get(j)));
        }

        MatOfPoint e = new MatOfPoint();
        e.fromList(listPo);
        hullPoints.add(e);

        List<MatOfPoint> defectPoints = new LinkedList<MatOfPoint>();
        List<Point> listPoDefect = new LinkedList<Point>();
        for (int j = 0; j < convexDefect.toList().size(); j = j+4) {
            Point farPoint = contours.get(boundPos).toList().get(convexDefect.toList().get(j+2));
            Integer depth = convexDefect.toList().get(j+3);
            if(depth > iThreshold && farPoint.y < a){
                listPoDefect.add(contours.get(boundPos).toList().get(convexDefect.toList().get(j+2)));
            }
            //Log.d(TAG, "defects ["+j+"] " + convexDefect.toList().get(j+3));

        }

        MatOfPoint e2 = new MatOfPoint();
        e2.fromList(listPo);
        defectPoints.add(e2);

        //Log.d(TAG, "hull: " + hull.toList());
        //Log.d(TAG, "defects: " + convexDefect.toList());


        Imgproc.drawContours(mRgba, hullPoints, -1, CONTOUR_COLOR, 3);

        int defectsTotal = (int) convexDefect.total();
        //Log.d(TAG, "Defect total " + defectsTotal);



        this.numberOfFingers = listPoDefect.size();
        if(this.numberOfFingers > 5) this.numberOfFingers = 5;



        mHandler.post(mUpdateFingerCountResults);



        //Log.d(TAG, "COORDINATES   "+ mRgba );
        counter++;
        // Handlers for buttons colors to return to default after any tap
        wHandlerQ.post(wLetterQ);
        wHandlerW.post(wLetterW);
        wHandlerE.post(wLetterE);
        wHandlerR.post(wLetterR);
        wHandlerT.post(wLetterT);
        wHandlerY.post(wLetterY);
        wHandlerU.post(wLetterU);
        wHandlerI.post(wLetterI);
        wHandlerO.post(wLetterO);
        wHandlerP.post(wLetterP);
        wHandlerA.post(wLetterA);
        wHandlerS.post(wLetterS);
        wHandlerD.post(wLetterD);
        wHandlerF.post(wLetterF);
        wHandlerG.post(wLetterG);
        wHandlerH.post(wLetterH);
        wHandlerJ.post(wLetterJ);
        wHandlerK.post(wLetterK);
        wHandlerL.post(wLetterL);
        wHandlerZ.post(wLetterZ);
        wHandlerX.post(wLetterX);
        wHandlerC.post(wLetterC);
        wHandlerV.post(wLetterV);
        wHandlerB.post(wLetterB);
        wHandlerN.post(wLetterN);
        wHandlerM.post(wLetterM);
        wHandlerSpace.post(wSpace);
        wHandlerBackSpace.post(wBackSpace);
        wHandlerTouch.post(wTouch);
        wHandlerMidAir.post(wMidAir);
        wHandlerTest.post(wTest);

        if (touchFlag == 0) { //Mid Air mode
            if(counter%7 == 0) { // Checking for new letter typed every 7 frames to avoid repeated typing
                if (testFlagAir == true) { // Mid Air test mode
                    // Tapping and writing in Mid Air Test mode
                    //Checking that the pointing finger only is raised and compare the finger coordinates and the
                    // button coordinates and type what the button pointed at contains
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterQ.getX() - 50 && listPoDefect.get(0).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterQ.getY() + 200 + 75) {
                        mHandlerQ.post(mLetterQ);
                        if (textView14.getText() == null) {
                            textView14.setText("Q");
                        } else {
                            textView14.setText(textView14.getText() + "Q");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    //This second condition checker where it checks for number of fingers == 2, is because the hand detection fluctuates
                    // so it is done to improve accuracy and takes into consideration the pointing finger only and neglects the other detected finger
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterQ.getX() - 50 && listPoDefect.get(0).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterQ.getY() + 200 + 75) {
                                mHandlerQ.post(mLetterQ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Q");
                                } else {
                                    textView14.setText(textView14.getText() + "Q");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterQ.getX() - 50 && listPoDefect.get(1).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterQ.getY() + 200 + 75) {
                                mHandlerQ.post(mLetterQ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Q");
                                } else {
                                    textView14.setText(textView14.getText() + "Q");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterW.getX() - 50 && listPoDefect.get(0).x < LetterW.getX() - 150 + 225 && listPoDefect.get(0).y > LetterW.getY() + 200 - 75 && listPoDefect.get(0).y < LetterW.getY() + 200 + 75) {
                        mHandlerW.post(mLetterW);
                        if (textView14.getText() == null) {
                            textView14.setText("W");
                        } else {
                            textView14.setText(textView14.getText() + "W");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterW.getX() - 50 && listPoDefect.get(0).x < LetterW.getX() - 150 + 225 && listPoDefect.get(0).y > LetterW.getY() + 200 - 75 && listPoDefect.get(0).y < LetterW.getY() + 200 + 75) {
                                mHandlerW.post(mLetterW);
                                if (textView14.getText() == null) {
                                    textView14.setText("W");
                                } else {
                                    textView14.setText(textView14.getText() + "W");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterW.getX() - 50 && listPoDefect.get(1).x < LetterW.getX() - 150 + 225 && listPoDefect.get(1).y > LetterW.getY() + 200 - 75 && listPoDefect.get(1).y < LetterW.getY() + 200 + 75) {
                                mHandlerW.post(mLetterW);
                                if (textView14.getText() == null) {
                                    textView14.setText("W");
                                } else {
                                    textView14.setText(textView14.getText() + "W");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterE.getX() - 50 && listPoDefect.get(0).x < LetterE.getX() - 150 + 225 && listPoDefect.get(0).y > LetterE.getY() + 200 - 75 && listPoDefect.get(0).y < LetterE.getY() + 200 + 75) {
                        mHandlerE.post(mLetterE);
                        if (textView14.getText() == null) {
                            textView14.setText("E");
                        } else {
                            textView14.setText(textView14.getText() + "E");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterE.getX() - 50 && listPoDefect.get(0).x < LetterE.getX() - 150 + 225 && listPoDefect.get(0).y > LetterE.getY() + 200 - 75 && listPoDefect.get(0).y < LetterE.getY() + 200 + 75) {
                                mHandlerE.post(mLetterE);
                                if (textView14.getText() == null) {
                                    textView14.setText("E");
                                } else {
                                    textView14.setText(textView14.getText() + "E");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterE.getX() - 50 && listPoDefect.get(1).x < LetterE.getX() - 150 + 225 && listPoDefect.get(1).y > LetterE.getY() + 200 - 75 && listPoDefect.get(1).y < LetterE.getY() + 200 + 75) {
                                mHandlerE.post(mLetterE);
                                if (textView14.getText() == null) {
                                    textView14.setText("E");
                                } else {
                                    textView14.setText(textView14.getText() + "E");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterR.getX() - 50 && listPoDefect.get(0).x < LetterR.getX() - 150 + 225 && listPoDefect.get(0).y > LetterR.getY() + 200 - 75 && listPoDefect.get(0).y < LetterR.getY() + 200 + 75) {
                        mHandlerR.post(mLetterR);
                        if (textView14.getText() == null) {
                            textView14.setText("R");
                        } else {
                            textView14.setText(textView14.getText() + "R");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterR.getX() - 50 && listPoDefect.get(0).x < LetterR.getX() - 150 + 225 && listPoDefect.get(0).y > LetterR.getY() + 200 - 75 && listPoDefect.get(0).y < LetterR.getY() + 200 + 75) {
                                mHandlerR.post(mLetterR);
                                if (textView14.getText() == null) {
                                    textView14.setText("R");
                                } else {
                                    textView14.setText(textView14.getText() + "R");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterR.getX() - 50 && listPoDefect.get(1).x < LetterR.getX() - 150 + 225 && listPoDefect.get(1).y > LetterR.getY() + 200 - 75 && listPoDefect.get(1).y < LetterR.getY() + 200 + 75) {
                                mHandlerR.post(mLetterR);
                                if (textView14.getText() == null) {
                                    textView14.setText("R");
                                } else {
                                    textView14.setText(textView14.getText() + "R");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterT.getX() - 50 && listPoDefect.get(0).x < LetterT.getX() - 150 + 225 && listPoDefect.get(0).y > LetterT.getY() + 200 - 75 && listPoDefect.get(0).y < LetterT.getY() + 200 + 75) {
                        mHandlerT.post(mLetterT);
                        if (textView14.getText() == null) {
                            textView14.setText("T");
                        } else {
                            textView14.setText(textView14.getText() + "T");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterT.getX() - 50 && listPoDefect.get(0).x < LetterT.getX() - 150 + 225 && listPoDefect.get(0).y > LetterT.getY() + 200 - 75 && listPoDefect.get(0).y < LetterT.getY() + 200 + 75) {
                                mHandlerT.post(mLetterT);
                                if (textView14.getText() == null) {
                                    textView14.setText("T");
                                } else {
                                    textView14.setText(textView14.getText() + "T");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterT.getX() - 50 && listPoDefect.get(1).x < LetterT.getX() - 150 + 225 && listPoDefect.get(1).y > LetterT.getY() + 200 - 75 && listPoDefect.get(1).y < LetterT.getY() + 200 + 75) {
                                mHandlerT.post(mLetterT);
                                if (textView14.getText() == null) {
                                    textView14.setText("T");
                                } else {
                                    textView14.setText(textView14.getText() + "T");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterY.getX() - 50 && listPoDefect.get(0).x < LetterY.getX() - 150 + 225 && listPoDefect.get(0).y > LetterY.getY() + 200 - 75 && listPoDefect.get(0).y < LetterY.getY() + 200 + 75) {
                        mHandlerY.post(mLetterY);
                        if (textView14.getText() == null) {
                            textView14.setText("Y");
                        } else {
                            textView14.setText(textView14.getText() + "Y");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterY.getX() - 50 && listPoDefect.get(0).x < LetterY.getX() - 150 + 225 && listPoDefect.get(0).y > LetterY.getY() + 200 - 75 && listPoDefect.get(0).y < LetterY.getY() + 200 + 75) {
                                mHandlerY.post(mLetterY);
                                if (textView14.getText() == null) {
                                    textView14.setText("Y");
                                } else {
                                    textView14.setText(textView14.getText() + "Y");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterY.getX() - 50 && listPoDefect.get(1).x < LetterY.getX() - 150 + 225 && listPoDefect.get(1).y > LetterY.getY() + 200 - 75 && listPoDefect.get(1).y < LetterY.getY() + 200 + 75) {
                                mHandlerY.post(mLetterY);
                                if (textView14.getText() == null) {
                                    textView14.setText("Y");
                                } else {
                                    textView14.setText(textView14.getText() + "Y");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterU.getX() - 50 && listPoDefect.get(0).x < LetterU.getX() - 150 + 225 && listPoDefect.get(0).y > LetterU.getY() + 200 - 75 && listPoDefect.get(0).y < LetterU.getY() + 200 + 75) {
                        mHandlerU.post(mLetterU);
                        if (textView14.getText() == null) {
                            textView14.setText("U");
                        } else {
                            textView14.setText(textView14.getText() + "U");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterU.getX() - 50 && listPoDefect.get(0).x < LetterU.getX() - 150 + 225 && listPoDefect.get(0).y > LetterU.getY() + 200 - 75 && listPoDefect.get(0).y < LetterU.getY() + 200 + 75) {
                                mHandlerU.post(mLetterU);
                                if (textView14.getText() == null) {
                                    textView14.setText("U");
                                } else {
                                    textView14.setText(textView14.getText() + "U");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterU.getX() - 50 && listPoDefect.get(1).x < LetterU.getX() - 150 + 225 && listPoDefect.get(1).y > LetterU.getY() + 200 - 75 && listPoDefect.get(1).y < LetterU.getY() + 200 + 75) {
                                mHandlerU.post(mLetterU);
                                if (textView14.getText() == null) {
                                    textView14.setText("U");
                                } else {
                                    textView14.setText(textView14.getText() + "U");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterI.getX() - 50 && listPoDefect.get(0).x < LetterI.getX() - 150 + 225 && listPoDefect.get(0).y > LetterI.getY() + 200 - 75 && listPoDefect.get(0).y < LetterI.getY() + 200 + 75) {
                        mHandlerI.post(mLetterI);
                        if (textView14.getText() == null) {
                            textView14.setText("I");
                        } else {
                            textView14.setText(textView14.getText() + "I");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterI.getX() - 50 && listPoDefect.get(0).x < LetterI.getX() - 150 + 225 && listPoDefect.get(0).y > LetterI.getY() + 200 - 75 && listPoDefect.get(0).y < LetterI.getY() + 200 + 75) {
                                mHandlerI.post(mLetterI);
                                if (textView14.getText() == null) {
                                    textView14.setText("I");
                                } else {
                                    textView14.setText(textView14.getText() + "I");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterI.getX() - 50 && listPoDefect.get(1).x < LetterI.getX() - 150 + 225 && listPoDefect.get(1).y > LetterI.getY() + 200 - 75 && listPoDefect.get(1).y < LetterI.getY() + 200 + 75) {
                                mHandlerI.post(mLetterI);
                                if (textView14.getText() == null) {
                                    textView14.setText("I");
                                } else {
                                    textView14.setText(textView14.getText() + "I");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterO.getX() - 50 && listPoDefect.get(0).x < LetterO.getX() - 150 + 225 && listPoDefect.get(0).y > LetterO.getY() + 200 - 75 && listPoDefect.get(0).y < LetterO.getY() + 200 + 75) {
                        mHandlerO.post(mLetterO);
                        if (textView14.getText() == null) {
                            textView14.setText("O");
                        } else {
                            textView14.setText(textView14.getText() + "O");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterO.getX() - 50 && listPoDefect.get(0).x < LetterO.getX() - 150 + 225 && listPoDefect.get(0).y > LetterO.getY() + 200 - 75 && listPoDefect.get(0).y < LetterO.getY() + 200 + 75) {
                                mHandlerO.post(mLetterO);
                                if (textView14.getText() == null) {
                                    textView14.setText("O");
                                } else {
                                    textView14.setText(textView14.getText() + "O");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterO.getX() - 50 && listPoDefect.get(1).x < LetterO.getX() - 150 + 225 && listPoDefect.get(1).y > LetterO.getY() + 200 - 75 && listPoDefect.get(1).y < LetterO.getY() + 200 + 75) {
                                mHandlerO.post(mLetterO);
                                if (textView14.getText() == null) {
                                    textView14.setText("O");
                                } else {
                                    textView14.setText(textView14.getText() + "O");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterP.getX() - 50 && listPoDefect.get(0).x < LetterP.getX() - 150 + 225 && listPoDefect.get(0).y > LetterP.getY() + 200 - 75 && listPoDefect.get(0).y < LetterP.getY() + 200 + 75) {
                        mHandlerP.post(mLetterP);
                        if (textView14.getText() == null) {
                            textView14.setText("P");
                        } else {
                            textView14.setText(textView14.getText() + "P");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterP.getX() - 50 && listPoDefect.get(0).x < LetterP.getX() - 150 + 225 && listPoDefect.get(0).y > LetterP.getY() + 200 - 75 && listPoDefect.get(0).y < LetterP.getY() + 200 + 75) {
                                mHandlerP.post(mLetterP);
                                if (textView14.getText() == null) {
                                    textView14.setText("P");
                                } else {
                                    textView14.setText(textView14.getText() + "P");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterP.getX() - 50 && listPoDefect.get(1).x < LetterP.getX() - 150 + 225 && listPoDefect.get(1).y > LetterP.getY() + 200 - 75 && listPoDefect.get(1).y < LetterP.getY() + 200 + 75) {
                                mHandlerP.post(mLetterP);
                                if (textView14.getText() == null) {
                                    textView14.setText("P");
                                } else {
                                    textView14.setText(textView14.getText() + "P");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterA.getX() - 50 && listPoDefect.get(0).x < LetterA.getX() - 150 + 225 && listPoDefect.get(0).y > LetterA.getY() + 200 - 75 && listPoDefect.get(0).y < LetterA.getY() + 200 + 75) {
                        mHandlerA.post(mLetterA);
                        if (textView14.getText() == null) {
                            textView14.setText("A");
                        } else {
                            textView14.setText(textView14.getText() + "A");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterA.getX() - 50 && listPoDefect.get(0).x < LetterA.getX() - 150 + 225 && listPoDefect.get(0).y > LetterA.getY() + 200 - 75 && listPoDefect.get(0).y < LetterA.getY() + 200 + 75) {
                                mHandlerA.post(mLetterA);
                                if (textView14.getText() == null) {
                                    textView14.setText("A");
                                } else {
                                    textView14.setText(textView14.getText() + "A");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterA.getX() - 50 && listPoDefect.get(1).x < LetterA.getX() - 150 + 225 && listPoDefect.get(1).y > LetterA.getY() + 200 - 75 && listPoDefect.get(1).y < LetterA.getY() + 200 + 75) {
                                mHandlerA.post(mLetterA);
                                if (textView14.getText() == null) {
                                    textView14.setText("A");
                                } else {
                                    textView14.setText(textView14.getText() + "A");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterS.getX() - 50 && listPoDefect.get(0).x < LetterS.getX() - 150 + 225 && listPoDefect.get(0).y > LetterS.getY() + 200 - 75 && listPoDefect.get(0).y < LetterS.getY() + 200 + 75) {
                        mHandlerS.post(mLetterS);
                        if (textView14.getText() == null) {
                            textView14.setText("S");
                        } else {
                            textView14.setText(textView14.getText() + "S");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterS.getX() - 50 && listPoDefect.get(0).x < LetterS.getX() - 150 + 225 && listPoDefect.get(0).y > LetterS.getY() + 200 - 75 && listPoDefect.get(0).y < LetterS.getY() + 200 + 75) {
                                mHandlerS.post(mLetterS);
                                if (textView14.getText() == null) {
                                    textView14.setText("S");
                                } else {
                                    textView14.setText(textView14.getText() + "S");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterS.getX() - 50 && listPoDefect.get(1).x < LetterS.getX() - 150 + 225 && listPoDefect.get(1).y > LetterS.getY() + 200 - 75 && listPoDefect.get(1).y < LetterS.getY() + 200 + 75) {
                                mHandlerS.post(mLetterS);
                                if (textView14.getText() == null) {
                                    textView14.setText("S");
                                } else {
                                    textView14.setText(textView14.getText() + "S");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterD.getX() - 50 && listPoDefect.get(0).x < LetterD.getX() - 150 + 225 && listPoDefect.get(0).y > LetterD.getY() + 200 - 75 && listPoDefect.get(0).y < LetterD.getY() + 200 + 75) {
                        mHandlerD.post(mLetterD);
                        if (textView14.getText() == null) {
                            textView14.setText("D");
                        } else {
                            textView14.setText(textView14.getText() + "D");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterD.getX() - 50 && listPoDefect.get(0).x < LetterD.getX() - 150 + 225 && listPoDefect.get(0).y > LetterD.getY() + 200 - 75 && listPoDefect.get(0).y < LetterD.getY() + 200 + 75) {
                                mHandlerD.post(mLetterD);
                                if (textView14.getText() == null) {
                                    textView14.setText("D");
                                } else {
                                    textView14.setText(textView14.getText() + "D");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterD.getX() - 50 && listPoDefect.get(1).x < LetterD.getX() - 150 + 225 && listPoDefect.get(1).y > LetterD.getY() + 200 - 75 && listPoDefect.get(1).y < LetterD.getY() + 200 + 75) {
                                mHandlerD.post(mLetterD);
                                if (textView14.getText() == null) {
                                    textView14.setText("D");
                                } else {
                                    textView14.setText(textView14.getText() + "D");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterF.getX() - 50 && listPoDefect.get(0).x < LetterF.getX() - 150 + 225 && listPoDefect.get(0).y > LetterF.getY() + 200 - 75 && listPoDefect.get(0).y < LetterF.getY() + 200 + 75) {
                        mHandlerF.post(mLetterF);
                        if (textView14.getText() == null) {
                            textView14.setText("F");
                        } else {
                            textView14.setText(textView14.getText() + "F");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterF.getX() - 50 && listPoDefect.get(0).x < LetterF.getX() - 150 + 225 && listPoDefect.get(0).y > LetterF.getY() + 200 - 75 && listPoDefect.get(0).y < LetterF.getY() + 200 + 75) {
                                mHandlerF.post(mLetterF);
                                if (textView14.getText() == null) {
                                    textView14.setText("F");
                                } else {
                                    textView14.setText(textView14.getText() + "F");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterF.getX() - 50 && listPoDefect.get(1).x < LetterF.getX() - 150 + 225 && listPoDefect.get(1).y > LetterF.getY() + 200 - 75 && listPoDefect.get(1).y < LetterF.getY() + 200 + 75) {
                                mHandlerF.post(mLetterF);
                                if (textView14.getText() == null) {
                                    textView14.setText("F");
                                } else {
                                    textView14.setText(textView14.getText() + "F");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterG.getX() - 50 && listPoDefect.get(0).x < LetterG.getX() - 150 + 225 && listPoDefect.get(0).y > LetterG.getY() + 200 - 75 && listPoDefect.get(0).y < LetterG.getY() + 200 + 75) {
                        mHandlerG.post(mLetterG);
                        if (textView14.getText() == null) {
                            textView14.setText("G");
                        } else {
                            textView14.setText(textView14.getText() + "G");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterG.getX() - 50 && listPoDefect.get(0).x < LetterG.getX() - 150 + 225 && listPoDefect.get(0).y > LetterG.getY() + 200 - 75 && listPoDefect.get(0).y < LetterG.getY() + 200 + 75) {
                                mHandlerG.post(mLetterG);
                                if (textView14.getText() == null) {
                                    textView14.setText("G");
                                } else {
                                    textView14.setText(textView14.getText() + "G");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterG.getX() - 50 && listPoDefect.get(1).x < LetterG.getX() - 150 + 225 && listPoDefect.get(1).y > LetterG.getY() + 200 - 75 && listPoDefect.get(1).y < LetterG.getY() + 200 + 75) {
                                mHandlerG.post(mLetterG);
                                if (textView14.getText() == null) {
                                    textView14.setText("G");
                                } else {
                                    textView14.setText(textView14.getText() + "G");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterH.getX() - 50 && listPoDefect.get(0).x < LetterH.getX() - 150 + 225 && listPoDefect.get(0).y > LetterH.getY() + 200 - 75 && listPoDefect.get(0).y < LetterH.getY() + 200 + 75) {
                        mHandlerH.post(mLetterH);
                        if (textView14.getText() == null) {
                            textView14.setText("H");
                        } else {
                            textView14.setText(textView14.getText() + "H");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterH.getX() - 50 && listPoDefect.get(0).x < LetterH.getX() - 150 + 225 && listPoDefect.get(0).y > LetterH.getY() + 200 - 75 && listPoDefect.get(0).y < LetterH.getY() + 200 + 75) {
                                mHandlerH.post(mLetterH);
                                if (textView14.getText() == null) {
                                    textView14.setText("H");
                                } else {
                                    textView14.setText(textView14.getText() + "H");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterH.getX() - 50 && listPoDefect.get(1).x < LetterH.getX() - 150 + 225 && listPoDefect.get(1).y > LetterH.getY() + 200 - 75 && listPoDefect.get(1).y < LetterH.getY() + 200 + 75) {
                                mHandlerH.post(mLetterH);
                                if (textView14.getText() == null) {
                                    textView14.setText("H");
                                } else {
                                    textView14.setText(textView14.getText() + "H");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterJ.getX() - 50 && listPoDefect.get(0).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterJ.getY() + 200 + 75) {
                        mHandlerJ.post(mLetterJ);
                        if (textView14.getText() == null) {
                            textView14.setText("J");
                        } else {
                            textView14.setText(textView14.getText() + "J");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterJ.getX() - 50 && listPoDefect.get(0).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterJ.getY() + 200 + 75) {
                                mHandlerJ.post(mLetterJ);
                                if (textView14.getText() == null) {
                                    textView14.setText("J");
                                } else {
                                    textView14.setText(textView14.getText() + "J");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterJ.getX() - 50 && listPoDefect.get(1).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterJ.getY() + 200 + 75) {
                                mHandlerJ.post(mLetterJ);
                                if (textView14.getText() == null) {
                                    textView14.setText("J");
                                } else {
                                    textView14.setText(textView14.getText() + "J");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterK.getX() - 50 && listPoDefect.get(0).x < LetterK.getX() - 150 + 225 && listPoDefect.get(0).y > LetterK.getY() + 200 - 75 && listPoDefect.get(0).y < LetterK.getY() + 200 + 75) {
                        mHandlerK.post(mLetterK);
                        if (textView14.getText() == null) {
                            textView14.setText("K");
                        } else {
                            textView14.setText(textView14.getText() + "K");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterK.getX() - 50 && listPoDefect.get(0).x < LetterK.getX() - 150 + 225 && listPoDefect.get(0).y > LetterK.getY() + 200 - 75 && listPoDefect.get(0).y < LetterK.getY() + 200 + 75) {
                                mHandlerK.post(mLetterK);
                                if (textView14.getText() == null) {
                                    textView14.setText("K");
                                } else {
                                    textView14.setText(textView14.getText() + "K");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterK.getX() - 50 && listPoDefect.get(1).x < LetterK.getX() - 150 + 225 && listPoDefect.get(1).y > LetterK.getY() + 200 - 75 && listPoDefect.get(1).y < LetterK.getY() + 200 + 75) {
                                mHandlerK.post(mLetterK);
                                if (textView14.getText() == null) {
                                    textView14.setText("K");
                                } else {
                                    textView14.setText(textView14.getText() + "K");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterL.getX() - 50 && listPoDefect.get(0).x < LetterL.getX() - 150 + 225 && listPoDefect.get(0).y > LetterL.getY() + 200 - 75 && listPoDefect.get(0).y < LetterL.getY() + 200 + 75) {
                        mHandlerL.post(mLetterL);
                        if (textView14.getText() == null) {
                            textView14.setText("L");
                        } else {
                            textView14.setText(textView14.getText() + "L");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterL.getX() - 50 && listPoDefect.get(0).x < LetterL.getX() - 150 + 225 && listPoDefect.get(0).y > LetterL.getY() + 200 - 75 && listPoDefect.get(0).y < LetterL.getY() + 200 + 75) {
                                mHandlerL.post(mLetterL);
                                if (textView14.getText() == null) {
                                    textView14.setText("L");
                                } else {
                                    textView14.setText(textView14.getText() + "L");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterL.getX() - 50 && listPoDefect.get(1).x < LetterL.getX() - 150 + 225 && listPoDefect.get(1).y > LetterL.getY() + 200 - 75 && listPoDefect.get(1).y < LetterL.getY() + 200 + 75) {
                                mHandlerL.post(mLetterL);
                                if (textView14.getText() == null) {
                                    textView14.setText("L");
                                } else {
                                    textView14.setText(textView14.getText() + "L");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterZ.getX() - 50 && listPoDefect.get(0).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterZ.getY() + 200 + 75) {
                        mHandlerZ.post(mLetterZ);
                        if (textView14.getText() == null) {
                            textView14.setText("Z");
                        } else {
                            textView14.setText(textView14.getText() + "Z");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterZ.getX() - 50 && listPoDefect.get(0).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterZ.getY() + 200 + 75) {
                                mHandlerZ.post(mLetterZ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Z");
                                } else {
                                    textView14.setText(textView14.getText() + "Z");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterZ.getX() - 50 && listPoDefect.get(1).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterZ.getY() + 200 + 75) {
                                mHandlerZ.post(mLetterZ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Z");
                                } else {
                                    textView14.setText(textView14.getText() + "Z");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterX.getX() - 50 && listPoDefect.get(0).x < LetterX.getX() - 150 + 225 && listPoDefect.get(0).y > LetterX.getY() + 200 - 75 && listPoDefect.get(0).y < LetterX.getY() + 200 + 75) {
                        mHandlerX.post(mLetterX);
                        if (textView14.getText() == null) {
                            textView14.setText("X");
                        } else {
                            textView14.setText(textView14.getText() + "X");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterX.getX() - 50 && listPoDefect.get(0).x < LetterX.getX() - 150 + 225 && listPoDefect.get(0).y > LetterX.getY() + 200 - 75 && listPoDefect.get(0).y < LetterX.getY() + 200 + 75) {
                                mHandlerX.post(mLetterX);
                                if (textView14.getText() == null) {
                                    textView14.setText("X");
                                } else {
                                    textView14.setText(textView14.getText() + "X");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterX.getX() - 50 && listPoDefect.get(1).x < LetterX.getX() - 150 + 225 && listPoDefect.get(1).y > LetterX.getY() + 200 - 75 && listPoDefect.get(1).y < LetterX.getY() + 200 + 75) {
                                mHandlerX.post(mLetterX);
                                if (textView14.getText() == null) {
                                    textView14.setText("X");
                                } else {
                                    textView14.setText(textView14.getText() + "X");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterC.getX() - 50 && listPoDefect.get(0).x < LetterC.getX() - 150 + 225 && listPoDefect.get(0).y > LetterC.getY() + 200 - 75 && listPoDefect.get(0).y < LetterC.getY() + 200 + 75) {
                        mHandlerC.post(mLetterC);
                        if (textView14.getText() == null) {
                            textView14.setText("C");
                        } else {
                            textView14.setText(textView14.getText() + "C");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterC.getX() - 50 && listPoDefect.get(0).x < LetterC.getX() - 150 + 225 && listPoDefect.get(0).y > LetterC.getY() + 200 - 75 && listPoDefect.get(0).y < LetterC.getY() + 200 + 75) {
                                mHandlerC.post(mLetterC);
                                if (textView14.getText() == null) {
                                    textView14.setText("C");
                                } else {
                                    textView14.setText(textView14.getText() + "C");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterC.getX() - 50 && listPoDefect.get(1).x < LetterC.getX() - 150 + 225 && listPoDefect.get(1).y > LetterC.getY() + 200 - 75 && listPoDefect.get(1).y < LetterC.getY() + 200 + 75) {
                                mHandlerC.post(mLetterC);
                                if (textView14.getText() == null) {
                                    textView14.setText("C");
                                } else {
                                    textView14.setText(textView14.getText() + "C");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterV.getX() - 50 && listPoDefect.get(0).x < LetterV.getX() - 150 + 225 && listPoDefect.get(0).y > LetterV.getY() + 200 - 75 && listPoDefect.get(0).y < LetterV.getY() + 200 + 75) {
                        mHandlerV.post(mLetterV);
                        if (textView14.getText() == null) {
                            textView14.setText("V");
                        } else {
                            textView14.setText(textView14.getText() + "V");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterV.getX() - 50 && listPoDefect.get(0).x < LetterV.getX() - 150 + 225 && listPoDefect.get(0).y > LetterV.getY() + 200 - 75 && listPoDefect.get(0).y < LetterV.getY() + 200 + 75) {
                                mHandlerV.post(mLetterV);
                                if (textView14.getText() == null) {
                                    textView14.setText("V");
                                } else {
                                    textView14.setText(textView14.getText() + "V");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterV.getX() - 50 && listPoDefect.get(1).x < LetterV.getX() - 150 + 225 && listPoDefect.get(1).y > LetterV.getY() + 200 - 75 && listPoDefect.get(1).y < LetterV.getY() + 200 + 75) {
                                mHandlerV.post(mLetterV);
                                if (textView14.getText() == null) {
                                    textView14.setText("V");
                                } else {
                                    textView14.setText(textView14.getText() + "V");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterB.getX() - 50 && listPoDefect.get(0).x < LetterB.getX() - 150 + 225 && listPoDefect.get(0).y > LetterB.getY() + 200 - 75 && listPoDefect.get(0).y < LetterB.getY() + 200 + 75) {
                        mHandlerB.post(mLetterB);
                        if (textView14.getText() == null) {
                            textView14.setText("B");
                        } else {
                            textView14.setText(textView14.getText() + "B");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterB.getX() - 50 && listPoDefect.get(0).x < LetterB.getX() - 150 + 225 && listPoDefect.get(0).y > LetterB.getY() + 200 - 75 && listPoDefect.get(0).y < LetterB.getY() + 200 + 75) {
                                mHandlerB.post(mLetterB);
                                if (textView14.getText() == null) {
                                    textView14.setText("B");
                                } else {
                                    textView14.setText(textView14.getText() + "B");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterB.getX() - 50 && listPoDefect.get(1).x < LetterB.getX() - 150 + 225 && listPoDefect.get(1).y > LetterB.getY() + 200 - 75 && listPoDefect.get(1).y < LetterB.getY() + 200 + 75) {
                                mHandlerB.post(mLetterB);
                                if (textView14.getText() == null) {
                                    textView14.setText("B");
                                } else {
                                    textView14.setText(textView14.getText() + "B");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterN.getX() - 50 && listPoDefect.get(0).x < LetterN.getX() - 150 + 225 && listPoDefect.get(0).y > LetterN.getY() + 200 - 75 && listPoDefect.get(0).y < LetterN.getY() + 200 + 75) {
                        mHandlerN.post(mLetterN);
                        if (textView14.getText() == null) {
                            textView14.setText("N");
                        } else {
                            textView14.setText(textView14.getText() + "N");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterN.getX() - 50 && listPoDefect.get(0).x < LetterN.getX() - 150 + 225 && listPoDefect.get(0).y > LetterN.getY() + 200 - 75 && listPoDefect.get(0).y < LetterN.getY() + 200 + 75) {
                                mHandlerN.post(mLetterN);
                                if (textView14.getText() == null) {
                                    textView14.setText("N");
                                } else {
                                    textView14.setText(textView14.getText() + "N");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterN.getX() - 50 && listPoDefect.get(1).x < LetterN.getX() - 150 + 225 && listPoDefect.get(1).y > LetterN.getY() + 200 - 75 && listPoDefect.get(1).y < LetterN.getY() + 200 + 75) {
                                mHandlerN.post(mLetterN);
                                if (textView14.getText() == null) {
                                    textView14.setText("N");
                                } else {
                                    textView14.setText(textView14.getText() + "N");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterM.getX() - 50 && listPoDefect.get(0).x < LetterM.getX() - 150 + 225 && listPoDefect.get(0).y > LetterM.getY() + 200 - 75 && listPoDefect.get(0).y < LetterM.getY() + 200 + 75) {
                        mHandlerM.post(mLetterM);
                        if (textView14.getText() == null) {
                            textView14.setText("M");

                        } else {
                            textView14.setText(textView14.getText() + "M");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterM.getX() - 50 && listPoDefect.get(0).x < LetterM.getX() - 150 + 225 && listPoDefect.get(0).y > LetterM.getY() + 200 - 75 && listPoDefect.get(0).y < LetterM.getY() + 200 + 75) {
                                mHandlerM.post(mLetterM);
                                if (textView14.getText() == null) {
                                    textView14.setText("M");
                                } else {
                                    textView14.setText(textView14.getText() + "M");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterM.getX() - 50 && listPoDefect.get(1).x < LetterM.getX() - 150 + 225 && listPoDefect.get(1).y > LetterM.getY() + 200 - 75 && listPoDefect.get(1).y < LetterM.getY() + 200 + 75) {
                                mHandlerM.post(mLetterM);
                                if (textView14.getText() == null) {
                                    textView14.setText("M");
                                } else {
                                    textView14.setText(textView14.getText() + "M");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Space.getX() - 50 && listPoDefect.get(0).x < Space.getX() - 150 + 225 && listPoDefect.get(0).y > Space.getY() + 200 - 75 && listPoDefect.get(0).y < Space.getY() + 200 + 75) {
                        mHandlerSpace.post(mSpace);
                        if (textView14.getText() == null) {
                            textView14.setText(" ");
                        } else {
                            textView14.setText(textView14.getText() + " ");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Space.getX() - 50 && listPoDefect.get(0).x < Space.getX() - 150 + 225 && listPoDefect.get(0).y > Space.getY() + 200 - 75 && listPoDefect.get(0).y < Space.getY() + 200 + 75) {
                                mHandlerSpace.post(mSpace);
                                if (textView14.getText() == null) {
                                    textView14.setText(" ");
                                } else {
                                    textView14.setText(textView14.getText() + " ");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > Space.getX() - 50 && listPoDefect.get(1).x < Space.getX() - 150 + 225 && listPoDefect.get(1).y > Space.getY() + 200 - 75 && listPoDefect.get(1).y < Space.getY() + 200 + 75) {
                                mHandlerSpace.post(mSpace);
                                if (textView14.getText() == null) {
                                    textView14.setText(" ");
                                } else {
                                    textView14.setText(textView14.getText() + " ");
                                }
                            }
                        }
                    }

                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Backspace.getX() - 50 && listPoDefect.get(0).x < Backspace.getX() - 150 + 100 && listPoDefect.get(0).y > Backspace.getY() + 200 && listPoDefect.get(0).y < Backspace.getY() + 200 + 100) {
                        mHandlerBackSpace.post(mBackSpace);
                        airError++;
                        if (textView14.length() > 0) {
                            textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                        } else {
                            textView14.setText("");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Backspace.getX() - 50 && listPoDefect.get(0).x < Backspace.getX() - 150 + 225 && listPoDefect.get(0).y > Backspace.getY() + 200 - 75 && listPoDefect.get(0).y < Backspace.getY() + 200 + 75) {
                                mHandlerBackSpace.post(mBackSpace);
                                airError++;
                                if (textView14.length() > 0) {
                                    textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                                } else {
                                    textView14.setText("");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > Backspace.getX() - 50 && listPoDefect.get(1).x < Backspace.getX() - 150 + 225 && listPoDefect.get(1).y > Backspace.getY() + 200 - 75 && listPoDefect.get(1).y < Backspace.getY() + 200 + 75) {
                                mHandlerBackSpace.post(mBackSpace);
                                airError++;
                                if (textView14.length() > 0) {
                                    textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                                } else {
                                    textView14.setText("");
                                }
                            }
                        }
                    }

                    // Switching between blocks in mid air test where each block contains 15 sentences
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Test.getX() - 50 && listPoDefect.get(0).x < Test.getX() - 150 + 100 && listPoDefect.get(0).y > Test.getY() + 100 && listPoDefect.get(0).y < Test.getY() + 200 + 100) {
                        testFlagAir = true;
                        mHandlerTest.post(mTest);
                        if (midAirBlock == 1) {
                            airBlock1();
                        }
                        if (midAirBlock == 2){
                            airBlock2();
                        }
                        if (midAirBlock == 3){
                            airBLock3();
                        }
                        if (midAirBlock == 4){
                            airBlock4();
                        }
                        if(midAirBlock == 5){
                            airBlock5();
                        }
                        if (midAirBlock == 6){
                            airBlock6();
                        }
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Test.getX() - 50 && listPoDefect.get(0).x < Test.getX() - 150 + 225 && listPoDefect.get(0).y > Test.getY() + 200 - 75 && listPoDefect.get(0).y < Test.getY() + 200 + 75) {
                                testFlagAir = true;
                                mHandlerTest.post(mTest);
                                if (midAirBlock == 1) {
                                    airBlock1();
                                }
                                if (midAirBlock == 2){
                                    airBlock2();
                                }
                                if (midAirBlock == 3){
                                    airBLock3();
                                }
                                if (midAirBlock == 4){
                                    airBlock4();
                                }
                                if(midAirBlock == 5){
                                    airBlock5();
                                }
                                if (midAirBlock == 6){
                                    airBlock6();
                                }

                            }
                        } else {
                            if (listPoDefect.get(1).x > Test.getX() - 50 && listPoDefect.get(1).x < Test.getX() - 150 + 225 && listPoDefect.get(1).y > Test.getY() + 200 - 75 && listPoDefect.get(1).y < Test.getY() + 200 + 75) {
                                testFlagAir = true;
                                mHandlerTest.post(mTest);
                                if (midAirBlock == 1) {
                                    airBlock1();
                                }
                                if (midAirBlock == 2){
                                    airBlock2();
                                }
                                if (midAirBlock == 3){
                                    airBLock3();
                                }
                                if (midAirBlock == 4){
                                    airBlock4();
                                }
                                if(midAirBlock == 5){
                                    airBlock5();
                                }
                                if (midAirBlock == 6){
                                    airBlock6();
                                }
                            }
                        }
                    }
                    endTimeAir = System.nanoTime();
                    elapsedTimeAir = elapsedTimeAir + (endTimeAir - startTimeAir) / 1000000000; // Measuring time for each sentence written in Mid Air test

                    //Typing in Mid Air mode outside the test mode

                } else {
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterQ.getX() - 50 && listPoDefect.get(0).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterQ.getY() + 200 + 75) {
                        mHandlerQ.post(mLetterQ);
                        if (textView14.getText() == null) {
                            textView14.setText("Q");
                        } else {
                            textView14.setText(textView14.getText() + "Q");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterQ.getX() - 50 && listPoDefect.get(0).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterQ.getY() + 200 + 75) {
                                mHandlerQ.post(mLetterQ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Q");
                                } else {
                                    textView14.setText(textView14.getText() + "Q");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterQ.getX() - 50 && listPoDefect.get(1).x < LetterQ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterQ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterQ.getY() + 200 + 75) {
                                mHandlerQ.post(mLetterQ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Q");
                                } else {
                                    textView14.setText(textView14.getText() + "Q");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterW.getX() - 50 && listPoDefect.get(0).x < LetterW.getX() - 150 + 225 && listPoDefect.get(0).y > LetterW.getY() + 200 - 75 && listPoDefect.get(0).y < LetterW.getY() + 200 + 75) {
                        mHandlerW.post(mLetterW);
                        if (textView14.getText() == null) {
                            textView14.setText("W");
                        } else {
                            textView14.setText(textView14.getText() + "W");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterW.getX() - 50 && listPoDefect.get(0).x < LetterW.getX() - 150 + 225 && listPoDefect.get(0).y > LetterW.getY() + 200 - 75 && listPoDefect.get(0).y < LetterW.getY() + 200 + 75) {
                                mHandlerW.post(mLetterW);
                                if (textView14.getText() == null) {
                                    textView14.setText("W");
                                } else {
                                    textView14.setText(textView14.getText() + "W");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterW.getX() - 50 && listPoDefect.get(1).x < LetterW.getX() - 150 + 225 && listPoDefect.get(1).y > LetterW.getY() + 200 - 75 && listPoDefect.get(1).y < LetterW.getY() + 200 + 75) {
                                mHandlerW.post(mLetterW);
                                if (textView14.getText() == null) {
                                    textView14.setText("W");
                                } else {
                                    textView14.setText(textView14.getText() + "W");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterE.getX() - 50 && listPoDefect.get(0).x < LetterE.getX() - 150 + 225 && listPoDefect.get(0).y > LetterE.getY() + 200 - 75 && listPoDefect.get(0).y < LetterE.getY() + 200 + 75) {
                        mHandlerE.post(mLetterE);
                        if (textView14.getText() == null) {
                            textView14.setText("E");
                        } else {
                            textView14.setText(textView14.getText() + "E");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterE.getX() - 50 && listPoDefect.get(0).x < LetterE.getX() - 150 + 225 && listPoDefect.get(0).y > LetterE.getY() + 200 - 75 && listPoDefect.get(0).y < LetterE.getY() + 200 + 75) {
                                mHandlerE.post(mLetterE);
                                if (textView14.getText() == null) {
                                    textView14.setText("E");
                                } else {
                                    textView14.setText(textView14.getText() + "E");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterE.getX() - 50 && listPoDefect.get(1).x < LetterE.getX() - 150 + 225 && listPoDefect.get(1).y > LetterE.getY() + 200 - 75 && listPoDefect.get(1).y < LetterE.getY() + 200 + 75) {
                                mHandlerE.post(mLetterE);
                                if (textView14.getText() == null) {
                                    textView14.setText("E");
                                } else {
                                    textView14.setText(textView14.getText() + "E");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterR.getX() - 50 && listPoDefect.get(0).x < LetterR.getX() - 150 + 225 && listPoDefect.get(0).y > LetterR.getY() + 200 - 75 && listPoDefect.get(0).y < LetterR.getY() + 200 + 75) {
                        mHandlerR.post(mLetterR);
                        if (textView14.getText() == null) {
                            textView14.setText("R");
                        } else {
                            textView14.setText(textView14.getText() + "R");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterR.getX() - 50 && listPoDefect.get(0).x < LetterR.getX() - 150 + 225 && listPoDefect.get(0).y > LetterR.getY() + 200 - 75 && listPoDefect.get(0).y < LetterR.getY() + 200 + 75) {
                                mHandlerR.post(mLetterR);
                                if (textView14.getText() == null) {
                                    textView14.setText("R");
                                } else {
                                    textView14.setText(textView14.getText() + "R");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterR.getX() - 50 && listPoDefect.get(1).x < LetterR.getX() - 150 + 225 && listPoDefect.get(1).y > LetterR.getY() + 200 - 75 && listPoDefect.get(1).y < LetterR.getY() + 200 + 75) {
                                mHandlerR.post(mLetterR);
                                if (textView14.getText() == null) {
                                    textView14.setText("R");
                                } else {
                                    textView14.setText(textView14.getText() + "R");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterT.getX() - 50 && listPoDefect.get(0).x < LetterT.getX() - 150 + 225 && listPoDefect.get(0).y > LetterT.getY() + 200 - 75 && listPoDefect.get(0).y < LetterT.getY() + 200 + 75) {
                        mHandlerT.post(mLetterT);
                        if (textView14.getText() == null) {
                            textView14.setText("T");
                        } else {
                            textView14.setText(textView14.getText() + "T");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterT.getX() - 50 && listPoDefect.get(0).x < LetterT.getX() - 150 + 225 && listPoDefect.get(0).y > LetterT.getY() + 200 - 75 && listPoDefect.get(0).y < LetterT.getY() + 200 + 75) {
                                mHandlerT.post(mLetterT);
                                if (textView14.getText() == null) {
                                    textView14.setText("T");
                                } else {
                                    textView14.setText(textView14.getText() + "T");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterT.getX() - 50 && listPoDefect.get(1).x < LetterT.getX() - 150 + 225 && listPoDefect.get(1).y > LetterT.getY() + 200 - 75 && listPoDefect.get(1).y < LetterT.getY() + 200 + 75) {
                                mHandlerT.post(mLetterT);
                                if (textView14.getText() == null) {
                                    textView14.setText("T");
                                } else {
                                    textView14.setText(textView14.getText() + "T");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterY.getX() - 50 && listPoDefect.get(0).x < LetterY.getX() - 150 + 225 && listPoDefect.get(0).y > LetterY.getY() + 200 - 75 && listPoDefect.get(0).y < LetterY.getY() + 200 + 75) {
                        mHandlerY.post(mLetterY);
                        if (textView14.getText() == null) {
                            textView14.setText("Y");
                        } else {
                            textView14.setText(textView14.getText() + "Y");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterY.getX() - 50 && listPoDefect.get(0).x < LetterY.getX() - 150 + 225 && listPoDefect.get(0).y > LetterY.getY() + 200 - 75 && listPoDefect.get(0).y < LetterY.getY() + 200 + 75) {
                                mHandlerY.post(mLetterY);
                                if (textView14.getText() == null) {
                                    textView14.setText("Y");
                                } else {
                                    textView14.setText(textView14.getText() + "Y");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterY.getX() - 50 && listPoDefect.get(1).x < LetterY.getX() - 150 + 225 && listPoDefect.get(1).y > LetterY.getY() + 200 - 75 && listPoDefect.get(1).y < LetterY.getY() + 200 + 75) {
                                mHandlerY.post(mLetterY);
                                if (textView14.getText() == null) {
                                    textView14.setText("Y");
                                } else {
                                    textView14.setText(textView14.getText() + "Y");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterU.getX() - 50 && listPoDefect.get(0).x < LetterU.getX() - 150 + 225 && listPoDefect.get(0).y > LetterU.getY() + 200 - 75 && listPoDefect.get(0).y < LetterU.getY() + 200 + 75) {
                        mHandlerU.post(mLetterU);
                        if (textView14.getText() == null) {
                            textView14.setText("U");
                        } else {
                            textView14.setText(textView14.getText() + "U");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterU.getX() - 50 && listPoDefect.get(0).x < LetterU.getX() - 150 + 225 && listPoDefect.get(0).y > LetterU.getY() + 200 - 75 && listPoDefect.get(0).y < LetterU.getY() + 200 + 75) {
                                mHandlerU.post(mLetterU);
                                if (textView14.getText() == null) {
                                    textView14.setText("U");
                                } else {
                                    textView14.setText(textView14.getText() + "U");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterU.getX() - 50 && listPoDefect.get(1).x < LetterU.getX() - 150 + 225 && listPoDefect.get(1).y > LetterU.getY() + 200 - 75 && listPoDefect.get(1).y < LetterU.getY() + 200 + 75) {
                                mHandlerU.post(mLetterU);
                                if (textView14.getText() == null) {
                                    textView14.setText("U");
                                } else {
                                    textView14.setText(textView14.getText() + "U");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterI.getX() - 50 && listPoDefect.get(0).x < LetterI.getX() - 150 + 225 && listPoDefect.get(0).y > LetterI.getY() + 200 - 75 && listPoDefect.get(0).y < LetterI.getY() + 200 + 75) {
                        mHandlerI.post(mLetterI);
                        if (textView14.getText() == null) {
                            textView14.setText("I");
                        } else {
                            textView14.setText(textView14.getText() + "I");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterI.getX() - 50 && listPoDefect.get(0).x < LetterI.getX() - 150 + 225 && listPoDefect.get(0).y > LetterI.getY() + 200 - 75 && listPoDefect.get(0).y < LetterI.getY() + 200 + 75) {
                                mHandlerI.post(mLetterI);
                                if (textView14.getText() == null) {
                                    textView14.setText("I");
                                } else {
                                    textView14.setText(textView14.getText() + "I");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterI.getX() - 50 && listPoDefect.get(1).x < LetterI.getX() - 150 + 225 && listPoDefect.get(1).y > LetterI.getY() + 200 - 75 && listPoDefect.get(1).y < LetterI.getY() + 200 + 75) {
                                mHandlerI.post(mLetterI);
                                if (textView14.getText() == null) {
                                    textView14.setText("I");
                                } else {
                                    textView14.setText(textView14.getText() + "I");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterO.getX() - 50 && listPoDefect.get(0).x < LetterO.getX() - 150 + 225 && listPoDefect.get(0).y > LetterO.getY() + 200 - 75 && listPoDefect.get(0).y < LetterO.getY() + 200 + 75) {
                        mHandlerO.post(mLetterO);
                        if (textView14.getText() == null) {
                            textView14.setText("O");
                        } else {
                            textView14.setText(textView14.getText() + "O");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterO.getX() - 50 && listPoDefect.get(0).x < LetterO.getX() - 150 + 225 && listPoDefect.get(0).y > LetterO.getY() + 200 - 75 && listPoDefect.get(0).y < LetterO.getY() + 200 + 75) {
                                mHandlerO.post(mLetterO);
                                if (textView14.getText() == null) {
                                    textView14.setText("O");
                                } else {
                                    textView14.setText(textView14.getText() + "O");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterO.getX() - 50 && listPoDefect.get(1).x < LetterO.getX() - 150 + 225 && listPoDefect.get(1).y > LetterO.getY() + 200 - 75 && listPoDefect.get(1).y < LetterO.getY() + 200 + 75) {
                                mHandlerO.post(mLetterO);
                                if (textView14.getText() == null) {
                                    textView14.setText("O");
                                } else {
                                    textView14.setText(textView14.getText() + "O");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterP.getX() - 50 && listPoDefect.get(0).x < LetterP.getX() - 150 + 225 && listPoDefect.get(0).y > LetterP.getY() + 200 - 75 && listPoDefect.get(0).y < LetterP.getY() + 200 + 75) {
                        mHandlerP.post(mLetterP);
                        if (textView14.getText() == null) {
                            textView14.setText("P");
                        } else {
                            textView14.setText(textView14.getText() + "P");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterP.getX() - 50 && listPoDefect.get(0).x < LetterP.getX() - 150 + 225 && listPoDefect.get(0).y > LetterP.getY() + 200 - 75 && listPoDefect.get(0).y < LetterP.getY() + 200 + 75) {
                                mHandlerP.post(mLetterP);
                                if (textView14.getText() == null) {
                                    textView14.setText("P");
                                } else {
                                    textView14.setText(textView14.getText() + "P");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterP.getX() - 50 && listPoDefect.get(1).x < LetterP.getX() - 150 + 225 && listPoDefect.get(1).y > LetterP.getY() + 200 - 75 && listPoDefect.get(1).y < LetterP.getY() + 200 + 75) {
                                mHandlerP.post(mLetterP);
                                if (textView14.getText() == null) {
                                    textView14.setText("P");
                                } else {
                                    textView14.setText(textView14.getText() + "P");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterA.getX() - 50 && listPoDefect.get(0).x < LetterA.getX() - 150 + 225 && listPoDefect.get(0).y > LetterA.getY() + 200 - 75 && listPoDefect.get(0).y < LetterA.getY() + 200 + 75) {
                        mHandlerA.post(mLetterA);
                        if (textView14.getText() == null) {
                            textView14.setText("A");
                        } else {
                            textView14.setText(textView14.getText() + "A");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterA.getX() - 50 && listPoDefect.get(0).x < LetterA.getX() - 150 + 225 && listPoDefect.get(0).y > LetterA.getY() + 200 - 75 && listPoDefect.get(0).y < LetterA.getY() + 200 + 75) {
                                mHandlerA.post(mLetterA);
                                if (textView14.getText() == null) {
                                    textView14.setText("A");
                                } else {
                                    textView14.setText(textView14.getText() + "A");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterA.getX() - 50 && listPoDefect.get(1).x < LetterA.getX() - 150 + 225 && listPoDefect.get(1).y > LetterA.getY() + 200 - 75 && listPoDefect.get(1).y < LetterA.getY() + 200 + 75) {
                                mHandlerA.post(mLetterA);
                                if (textView14.getText() == null) {
                                    textView14.setText("A");
                                } else {
                                    textView14.setText(textView14.getText() + "A");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterS.getX() - 50 && listPoDefect.get(0).x < LetterS.getX() - 150 + 225 && listPoDefect.get(0).y > LetterS.getY() + 200 - 75 && listPoDefect.get(0).y < LetterS.getY() + 200 + 75) {
                        mHandlerS.post(mLetterS);
                        if (textView14.getText() == null) {
                            textView14.setText("S");
                        } else {
                            textView14.setText(textView14.getText() + "S");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterS.getX() - 50 && listPoDefect.get(0).x < LetterS.getX() - 150 + 225 && listPoDefect.get(0).y > LetterS.getY() + 200 - 75 && listPoDefect.get(0).y < LetterS.getY() + 200 + 75) {
                                mHandlerS.post(mLetterS);
                                if (textView14.getText() == null) {
                                    textView14.setText("S");
                                } else {
                                    textView14.setText(textView14.getText() + "S");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterS.getX() - 50 && listPoDefect.get(1).x < LetterS.getX() - 150 + 225 && listPoDefect.get(1).y > LetterS.getY() + 200 - 75 && listPoDefect.get(1).y < LetterS.getY() + 200 + 75) {
                                mHandlerS.post(mLetterS);
                                if (textView14.getText() == null) {
                                    textView14.setText("S");
                                } else {
                                    textView14.setText(textView14.getText() + "S");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterD.getX() - 50 && listPoDefect.get(0).x < LetterD.getX() - 150 + 225 && listPoDefect.get(0).y > LetterD.getY() + 200 - 75 && listPoDefect.get(0).y < LetterD.getY() + 200 + 75) {
                        mHandlerD.post(mLetterD);
                        if (textView14.getText() == null) {
                            textView14.setText("D");
                        } else {
                            textView14.setText(textView14.getText() + "D");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterD.getX() - 50 && listPoDefect.get(0).x < LetterD.getX() - 150 + 225 && listPoDefect.get(0).y > LetterD.getY() + 200 - 75 && listPoDefect.get(0).y < LetterD.getY() + 200 + 75) {
                                mHandlerD.post(mLetterD);
                                if (textView14.getText() == null) {
                                    textView14.setText("D");
                                } else {
                                    textView14.setText(textView14.getText() + "D");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterD.getX() - 50 && listPoDefect.get(1).x < LetterD.getX() - 150 + 225 && listPoDefect.get(1).y > LetterD.getY() + 200 - 75 && listPoDefect.get(1).y < LetterD.getY() + 200 + 75) {
                                mHandlerD.post(mLetterD);
                                if (textView14.getText() == null) {
                                    textView14.setText("D");
                                } else {
                                    textView14.setText(textView14.getText() + "D");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterF.getX() - 50 && listPoDefect.get(0).x < LetterF.getX() - 150 + 225 && listPoDefect.get(0).y > LetterF.getY() + 200 - 75 && listPoDefect.get(0).y < LetterF.getY() + 200 + 75) {
                        mHandlerF.post(mLetterF);
                        if (textView14.getText() == null) {
                            textView14.setText("F");
                        } else {
                            textView14.setText(textView14.getText() + "F");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterF.getX() - 50 && listPoDefect.get(0).x < LetterF.getX() - 150 + 225 && listPoDefect.get(0).y > LetterF.getY() + 200 - 75 && listPoDefect.get(0).y < LetterF.getY() + 200 + 75) {
                                mHandlerF.post(mLetterF);
                                if (textView14.getText() == null) {
                                    textView14.setText("F");
                                } else {
                                    textView14.setText(textView14.getText() + "F");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterF.getX() - 50 && listPoDefect.get(1).x < LetterF.getX() - 150 + 225 && listPoDefect.get(1).y > LetterF.getY() + 200 - 75 && listPoDefect.get(1).y < LetterF.getY() + 200 + 75) {
                                mHandlerF.post(mLetterF);
                                if (textView14.getText() == null) {
                                    textView14.setText("F");
                                } else {
                                    textView14.setText(textView14.getText() + "F");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterG.getX() - 50 && listPoDefect.get(0).x < LetterG.getX() - 150 + 225 && listPoDefect.get(0).y > LetterG.getY() + 200 - 75 && listPoDefect.get(0).y < LetterG.getY() + 200 + 75) {
                        mHandlerG.post(mLetterG);
                        if (textView14.getText() == null) {
                            textView14.setText("G");
                        } else {
                            textView14.setText(textView14.getText() + "G");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterG.getX() - 50 && listPoDefect.get(0).x < LetterG.getX() - 150 + 225 && listPoDefect.get(0).y > LetterG.getY() + 200 - 75 && listPoDefect.get(0).y < LetterG.getY() + 200 + 75) {
                                mHandlerG.post(mLetterG);
                                if (textView14.getText() == null) {
                                    textView14.setText("G");
                                } else {
                                    textView14.setText(textView14.getText() + "G");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterG.getX() - 50 && listPoDefect.get(1).x < LetterG.getX() - 150 + 225 && listPoDefect.get(1).y > LetterG.getY() + 200 - 75 && listPoDefect.get(1).y < LetterG.getY() + 200 + 75) {
                                mHandlerG.post(mLetterG);
                                if (textView14.getText() == null) {
                                    textView14.setText("G");
                                } else {
                                    textView14.setText(textView14.getText() + "G");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterH.getX() - 50 && listPoDefect.get(0).x < LetterH.getX() - 150 + 225 && listPoDefect.get(0).y > LetterH.getY() + 200 - 75 && listPoDefect.get(0).y < LetterH.getY() + 200 + 75) {
                        mHandlerH.post(mLetterH);
                        if (textView14.getText() == null) {
                            textView14.setText("H");
                        } else {
                            textView14.setText(textView14.getText() + "H");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterH.getX() - 50 && listPoDefect.get(0).x < LetterH.getX() - 150 + 225 && listPoDefect.get(0).y > LetterH.getY() + 200 - 75 && listPoDefect.get(0).y < LetterH.getY() + 200 + 75) {
                                mHandlerH.post(mLetterH);
                                if (textView14.getText() == null) {
                                    textView14.setText("H");
                                } else {
                                    textView14.setText(textView14.getText() + "H");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterH.getX() - 50 && listPoDefect.get(1).x < LetterH.getX() - 150 + 225 && listPoDefect.get(1).y > LetterH.getY() + 200 - 75 && listPoDefect.get(1).y < LetterH.getY() + 200 + 75) {
                                mHandlerH.post(mLetterH);
                                if (textView14.getText() == null) {
                                    textView14.setText("H");
                                } else {
                                    textView14.setText(textView14.getText() + "H");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterJ.getX() - 50 && listPoDefect.get(0).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterJ.getY() + 200 + 75) {
                        mHandlerJ.post(mLetterJ);
                        if (textView14.getText() == null) {
                            textView14.setText("J");
                        } else {
                            textView14.setText(textView14.getText() + "J");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterJ.getX() - 50 && listPoDefect.get(0).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterJ.getY() + 200 + 75) {
                                mHandlerJ.post(mLetterJ);
                                if (textView14.getText() == null) {
                                    textView14.setText("J");
                                } else {
                                    textView14.setText(textView14.getText() + "J");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterJ.getX() - 50 && listPoDefect.get(1).x < LetterJ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterJ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterJ.getY() + 200 + 75) {
                                mHandlerJ.post(mLetterJ);
                                if (textView14.getText() == null) {
                                    textView14.setText("J");
                                } else {
                                    textView14.setText(textView14.getText() + "J");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterK.getX() - 50 && listPoDefect.get(0).x < LetterK.getX() - 150 + 225 && listPoDefect.get(0).y > LetterK.getY() + 200 - 75 && listPoDefect.get(0).y < LetterK.getY() + 200 + 75) {
                        mHandlerK.post(mLetterK);
                        if (textView14.getText() == null) {
                            textView14.setText("K");
                        } else {
                            textView14.setText(textView14.getText() + "K");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterK.getX() - 50 && listPoDefect.get(0).x < LetterK.getX() - 150 + 225 && listPoDefect.get(0).y > LetterK.getY() + 200 - 75 && listPoDefect.get(0).y < LetterK.getY() + 200 + 75) {
                                mHandlerK.post(mLetterK);
                                if (textView14.getText() == null) {
                                    textView14.setText("K");
                                } else {
                                    textView14.setText(textView14.getText() + "K");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterK.getX() - 50 && listPoDefect.get(1).x < LetterK.getX() - 150 + 225 && listPoDefect.get(1).y > LetterK.getY() + 200 - 75 && listPoDefect.get(1).y < LetterK.getY() + 200 + 75) {
                                mHandlerK.post(mLetterK);
                                if (textView14.getText() == null) {
                                    textView14.setText("K");
                                } else {
                                    textView14.setText(textView14.getText() + "K");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterL.getX() - 50 && listPoDefect.get(0).x < LetterL.getX() - 150 + 225 && listPoDefect.get(0).y > LetterL.getY() + 200 - 75 && listPoDefect.get(0).y < LetterL.getY() + 200 + 75) {
                        mHandlerL.post(mLetterL);
                        if (textView14.getText() == null) {
                            textView14.setText("L");
                        } else {
                            textView14.setText(textView14.getText() + "L");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterL.getX() - 50 && listPoDefect.get(0).x < LetterL.getX() - 150 + 225 && listPoDefect.get(0).y > LetterL.getY() + 200 - 75 && listPoDefect.get(0).y < LetterL.getY() + 200 + 75) {
                                mHandlerL.post(mLetterL);
                                if (textView14.getText() == null) {
                                    textView14.setText("L");
                                } else {
                                    textView14.setText(textView14.getText() + "L");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterL.getX() - 50 && listPoDefect.get(1).x < LetterL.getX() - 150 + 225 && listPoDefect.get(1).y > LetterL.getY() + 200 - 75 && listPoDefect.get(1).y < LetterL.getY() + 200 + 75) {
                                mHandlerL.post(mLetterL);
                                if (textView14.getText() == null) {
                                    textView14.setText("L");
                                } else {
                                    textView14.setText(textView14.getText() + "L");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterZ.getX() - 50 && listPoDefect.get(0).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterZ.getY() + 200 + 75) {
                        mHandlerZ.post(mLetterZ);
                        if (textView14.getText() == null) {
                            textView14.setText("Z");
                        } else {
                            textView14.setText(textView14.getText() + "Z");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterZ.getX() - 50 && listPoDefect.get(0).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(0).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(0).y < LetterZ.getY() + 200 + 75) {
                                mHandlerZ.post(mLetterZ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Z");
                                } else {
                                    textView14.setText(textView14.getText() + "Z");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterZ.getX() - 50 && listPoDefect.get(1).x < LetterZ.getX() - 150 + 225 && listPoDefect.get(1).y > LetterZ.getY() + 200 - 75 && listPoDefect.get(1).y < LetterZ.getY() + 200 + 75) {
                                mHandlerZ.post(mLetterZ);
                                if (textView14.getText() == null) {
                                    textView14.setText("Z");
                                } else {
                                    textView14.setText(textView14.getText() + "Z");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterX.getX() - 50 && listPoDefect.get(0).x < LetterX.getX() - 150 + 225 && listPoDefect.get(0).y > LetterX.getY() + 200 - 75 && listPoDefect.get(0).y < LetterX.getY() + 200 + 75) {
                        mHandlerX.post(mLetterX);
                        if (textView14.getText() == null) {
                            textView14.setText("X");
                        } else {
                            textView14.setText(textView14.getText() + "X");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterX.getX() - 50 && listPoDefect.get(0).x < LetterX.getX() - 150 + 225 && listPoDefect.get(0).y > LetterX.getY() + 200 - 75 && listPoDefect.get(0).y < LetterX.getY() + 200 + 75) {
                                mHandlerX.post(mLetterX);
                                if (textView14.getText() == null) {
                                    textView14.setText("X");
                                } else {
                                    textView14.setText(textView14.getText() + "X");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterX.getX() - 50 && listPoDefect.get(1).x < LetterX.getX() - 150 + 225 && listPoDefect.get(1).y > LetterX.getY() + 200 - 75 && listPoDefect.get(1).y < LetterX.getY() + 200 + 75) {
                                mHandlerX.post(mLetterX);
                                if (textView14.getText() == null) {
                                    textView14.setText("X");
                                } else {
                                    textView14.setText(textView14.getText() + "X");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterC.getX() - 50 && listPoDefect.get(0).x < LetterC.getX() - 150 + 225 && listPoDefect.get(0).y > LetterC.getY() + 200 - 75 && listPoDefect.get(0).y < LetterC.getY() + 200 + 75) {
                        mHandlerC.post(mLetterC);
                        if (textView14.getText() == null) {
                            textView14.setText("C");
                        } else {
                            textView14.setText(textView14.getText() + "C");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterC.getX() - 50 && listPoDefect.get(0).x < LetterC.getX() - 150 + 225 && listPoDefect.get(0).y > LetterC.getY() + 200 - 75 && listPoDefect.get(0).y < LetterC.getY() + 200 + 75) {
                                mHandlerC.post(mLetterC);
                                if (textView14.getText() == null) {
                                    textView14.setText("C");
                                } else {
                                    textView14.setText(textView14.getText() + "C");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterC.getX() - 50 && listPoDefect.get(1).x < LetterC.getX() - 150 + 225 && listPoDefect.get(1).y > LetterC.getY() + 200 - 75 && listPoDefect.get(1).y < LetterC.getY() + 200 + 75) {
                                mHandlerC.post(mLetterC);
                                if (textView14.getText() == null) {
                                    textView14.setText("C");
                                } else {
                                    textView14.setText(textView14.getText() + "C");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterV.getX() - 50 && listPoDefect.get(0).x < LetterV.getX() - 150 + 225 && listPoDefect.get(0).y > LetterV.getY() + 200 - 75 && listPoDefect.get(0).y < LetterV.getY() + 200 + 75) {
                        mHandlerV.post(mLetterV);
                        if (textView14.getText() == null) {
                            textView14.setText("V");
                        } else {
                            textView14.setText(textView14.getText() + "V");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterV.getX() - 50 && listPoDefect.get(0).x < LetterV.getX() - 150 + 225 && listPoDefect.get(0).y > LetterV.getY() + 200 - 75 && listPoDefect.get(0).y < LetterV.getY() + 200 + 75) {
                                mHandlerV.post(mLetterV);
                                if (textView14.getText() == null) {
                                    textView14.setText("V");
                                } else {
                                    textView14.setText(textView14.getText() + "V");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterV.getX() - 50 && listPoDefect.get(1).x < LetterV.getX() - 150 + 225 && listPoDefect.get(1).y > LetterV.getY() + 200 - 75 && listPoDefect.get(1).y < LetterV.getY() + 200 + 75) {
                                mHandlerV.post(mLetterV);
                                if (textView14.getText() == null) {
                                    textView14.setText("V");
                                } else {
                                    textView14.setText(textView14.getText() + "V");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterB.getX() - 50 && listPoDefect.get(0).x < LetterB.getX() - 150 + 225 && listPoDefect.get(0).y > LetterB.getY() + 200 - 75 && listPoDefect.get(0).y < LetterB.getY() + 200 + 75) {
                        mHandlerB.post(mLetterB);
                        if (textView14.getText() == null) {
                            textView14.setText("B");
                        } else {
                            textView14.setText(textView14.getText() + "B");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterB.getX() - 50 && listPoDefect.get(0).x < LetterB.getX() - 150 + 225 && listPoDefect.get(0).y > LetterB.getY() + 200 - 75 && listPoDefect.get(0).y < LetterB.getY() + 200 + 75) {
                                mHandlerB.post(mLetterB);
                                if (textView14.getText() == null) {
                                    textView14.setText("B");
                                } else {
                                    textView14.setText(textView14.getText() + "B");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterB.getX() - 50 && listPoDefect.get(1).x < LetterB.getX() - 150 + 225 && listPoDefect.get(1).y > LetterB.getY() + 200 - 75 && listPoDefect.get(1).y < LetterB.getY() + 200 + 75) {
                                mHandlerB.post(mLetterB);
                                if (textView14.getText() == null) {
                                    textView14.setText("B");
                                } else {
                                    textView14.setText(textView14.getText() + "B");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterN.getX() - 50 && listPoDefect.get(0).x < LetterN.getX() - 150 + 225 && listPoDefect.get(0).y > LetterN.getY() + 200 - 75 && listPoDefect.get(0).y < LetterN.getY() + 200 + 75) {
                        mHandlerN.post(mLetterN);
                        if (textView14.getText() == null) {
                            textView14.setText("N");
                        } else {
                            textView14.setText(textView14.getText() + "N");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterN.getX() - 50 && listPoDefect.get(0).x < LetterN.getX() - 150 + 225 && listPoDefect.get(0).y > LetterN.getY() + 200 - 75 && listPoDefect.get(0).y < LetterN.getY() + 200 + 75) {
                                mHandlerN.post(mLetterN);
                                if (textView14.getText() == null) {
                                    textView14.setText("N");
                                } else {
                                    textView14.setText(textView14.getText() + "N");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterN.getX() - 50 && listPoDefect.get(1).x < LetterN.getX() - 150 + 225 && listPoDefect.get(1).y > LetterN.getY() + 200 - 75 && listPoDefect.get(1).y < LetterN.getY() + 200 + 75) {
                                mHandlerN.post(mLetterN);
                                if (textView14.getText() == null) {
                                    textView14.setText("N");
                                } else {
                                    textView14.setText(textView14.getText() + "N");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > LetterM.getX() - 50 && listPoDefect.get(0).x < LetterM.getX() - 150 + 225 && listPoDefect.get(0).y > LetterM.getY() + 200 - 75 && listPoDefect.get(0).y < LetterM.getY() + 200 + 75) {
                        mHandlerM.post(mLetterM);
                        if (textView14.getText() == null) {
                            textView14.setText("M");

                        } else {
                            textView14.setText(textView14.getText() + "M");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }
                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > LetterM.getX() - 50 && listPoDefect.get(0).x < LetterM.getX() - 150 + 225 && listPoDefect.get(0).y > LetterM.getY() + 200 - 75 && listPoDefect.get(0).y < LetterM.getY() + 200 + 75) {
                                mHandlerM.post(mLetterM);
                                if (textView14.getText() == null) {
                                    textView14.setText("M");
                                } else {
                                    textView14.setText(textView14.getText() + "M");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > LetterM.getX() - 50 && listPoDefect.get(1).x < LetterM.getX() - 150 + 225 && listPoDefect.get(1).y > LetterM.getY() + 200 - 75 && listPoDefect.get(1).y < LetterM.getY() + 200 + 75) {
                                mHandlerM.post(mLetterM);
                                if (textView14.getText() == null) {
                                    textView14.setText("M");
                                } else {
                                    textView14.setText(textView14.getText() + "M");
                                }
                            }
                        }
                    }
                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Space.getX() - 50 && listPoDefect.get(0).x < Space.getX() - 150 + 225 && listPoDefect.get(0).y > Space.getY() + 200 - 75 && listPoDefect.get(0).y < Space.getY() + 200 + 75) {
                        mHandlerSpace.post(mSpace);
                        if (textView14.getText() == null) {
                            textView14.setText(" ");
                        } else {
                            textView14.setText(textView14.getText() + " ");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Space.getX() - 50 && listPoDefect.get(0).x < Space.getX() - 150 + 225 && listPoDefect.get(0).y > Space.getY() + 200 - 75 && listPoDefect.get(0).y < Space.getY() + 200 + 75) {
                                mHandlerSpace.post(mSpace);
                                if (textView14.getText() == null) {
                                    textView14.setText(" ");
                                } else {
                                    textView14.setText(textView14.getText() + " ");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > Space.getX() - 50 && listPoDefect.get(1).x < Space.getX() - 150 + 225 && listPoDefect.get(1).y > Space.getY() + 200 - 75 && listPoDefect.get(1).y < Space.getY() + 200 + 75) {
                                mHandlerSpace.post(mSpace);
                                if (textView14.getText() == null) {
                                    textView14.setText(" ");
                                } else {
                                    textView14.setText(textView14.getText() + " ");
                                }
                            }
                        }
                    }

                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Backspace.getX() - 50 && listPoDefect.get(0).x < Backspace.getX() - 150 + 100 && listPoDefect.get(0).y > Backspace.getY() + 200 && listPoDefect.get(0).y < Backspace.getY() + 200 + 100) {
                        mHandlerBackSpace.post(mBackSpace);
                        airError++;
                        if (textView14.length() > 0) {
                            textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                        } else {
                            textView14.setText("");
                        }
                        //Log.d(TAG, "Letter " + textView14.getX());
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Backspace.getX() - 50 && listPoDefect.get(0).x < Backspace.getX() - 150 + 225 && listPoDefect.get(0).y > Backspace.getY() + 200 - 75 && listPoDefect.get(0).y < Backspace.getY() + 200 + 75) {
                                mHandlerBackSpace.post(mBackSpace);
                                airError++;
                                if (textView14.length() > 0) {
                                    textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                                } else {
                                    textView14.setText("");
                                }
                            }
                        } else {
                            if (listPoDefect.get(1).x > Backspace.getX() - 50 && listPoDefect.get(1).x < Backspace.getX() - 150 + 225 && listPoDefect.get(1).y > Backspace.getY() + 200 - 75 && listPoDefect.get(1).y < Backspace.getY() + 200 + 75) {
                                mHandlerBackSpace.post(mBackSpace);
                                airError++;
                                if (textView14.length() > 0) {
                                    textView14.setText(textView14.getText().toString().substring(0, textView14.getText().length() - 1));
                                } else {
                                    textView14.setText("");
                                }
                            }
                        }
                    }

                    if ((this.numberOfFingers == 1) && listPoDefect.get(0).x > Test.getX() - 50 && listPoDefect.get(0).x < Test.getX() - 150 + 100 && listPoDefect.get(0).y > Test.getY() + 100 && listPoDefect.get(0).y < Test.getY() + 200 + 100) {
                        testFlagAir = true;
                        mHandlerTest.post(mTest);
                        textView14.setText("BLOCK 1 ;");
                    }

                    if (this.numberOfFingers == 2) {
                        if (listPoDefect.get(0).y < listPoDefect.get(1).y) {
                            if (listPoDefect.get(0).x > Test.getX() - 50 && listPoDefect.get(0).x < Test.getX() - 150 + 225 && listPoDefect.get(0).y > Test.getY() + 200 - 75 && listPoDefect.get(0).y < Test.getY() + 200 + 75) {
                                testFlagAir = true;
                                mHandlerTest.post(mTest);
                                textView14.setText("BLOCK 1 ;");
                            }
                        } else {
                            if (listPoDefect.get(1).x > Test.getX() - 50 && listPoDefect.get(1).x < Test.getX() - 150 + 225 && listPoDefect.get(1).y > Test.getY() + 200 - 75 && listPoDefect.get(1).y < Test.getY() + 200 + 75) {
                                testFlagAir = true;
                                mHandlerTest.post(mTest);
                                textView14.setText("BLOCK 1 ;");
                            }
                        }
                    }
                }
            }

        }

        /*File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        dir.mkdirs();
        String filename = "MidAirTest.txt";
        File file = new File(dir, filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("MidAir elapsed time : " + Long.toString(elapsedTimeAir) + "\n" + "MidAir Errors : " + airError);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/



        //Log.d(TAG, "Letter " + LetterA.getY());


        //mHandler2.post(mLetterA);
        //Log.d(TAG, "Defect total " + this.numberOfFingers);

        for(Point p : listPoDefect){
            Imgproc.circle(mRgba, p, 6, new Scalar(0,0,255));
        }

        return mRgba;
    }

    public void updateNumberOfFingers(){
        numberOfFingersText.setText(String.valueOf(this.numberOfFingers));
    }
    // Methods for changing background color of button to red when tapped
    public void letterQ(){
        LetterQ.setBackgroundColor(0xfff00000);
    }
    public void letterW(){
        LetterW.setBackgroundColor(0xfff00000);
    }
    public void letterE(){
        LetterE.setBackgroundColor(0xfff00000);
    }
    public void letterR(){
        LetterR.setBackgroundColor(0xfff00000);
    }
    public void letterT(){
        LetterT.setBackgroundColor(0xfff00000);
    }
    public void letterY(){
        LetterY.setBackgroundColor(0xfff00000);
    }
    public void letterU(){
        LetterU.setBackgroundColor(0xfff00000);
    }
    public void letterI(){
        LetterI.setBackgroundColor(0xfff00000);
    }
    public void letterO(){
        LetterO.setBackgroundColor(0xfff00000);
    }
    public void letterP(){
        LetterP.setBackgroundColor(0xfff00000);
    }
    public void letterA(){
        LetterA.setBackgroundColor(0xfff00000);
    }
    public void letterS(){
        LetterS.setBackgroundColor(0xfff00000);
    }
    public void letterD(){
        LetterD.setBackgroundColor(0xfff00000);
    }
    public void letterF(){
        LetterF.setBackgroundColor(0xfff00000);
    }
    public void letterG(){
        LetterG.setBackgroundColor(0xfff00000);
    }
    public void letterH(){
        LetterH.setBackgroundColor(0xfff00000);
    }
    public void letterJ(){
        LetterJ.setBackgroundColor(0xfff00000);
    }
    public void letterK(){
        LetterK.setBackgroundColor(0xfff00000);
    }
    public void letterL(){
        LetterL.setBackgroundColor(0xfff00000);
    }
    public void letterZ(){
        LetterZ.setBackgroundColor(0xfff00000);
    }
    public void letterX(){
        LetterX.setBackgroundColor(0xfff00000);
    }
    public void letterC(){
        LetterC.setBackgroundColor(0xfff00000);
    }
    public void letterV(){
        LetterV.setBackgroundColor(0xfff00000);
    }
    public void letterB(){
        LetterB.setBackgroundColor(0xfff00000);
    }
    public void letterN(){
        LetterN.setBackgroundColor(0xfff00000);
    }
    public void letterM(){
        LetterM.setBackgroundColor(0xfff00000);
    }
    public void Space(){
        Space.setBackgroundColor(0xfff00000);
    }
    public void backSpace(){
        Backspace.setBackgroundColor(0xfff00000);
    }
    public void touch(){
        Touch.setBackgroundColor(0xfff00000);
    }
    public void midAir(){
        MidAir.setBackgroundColor(0xfff00000);
    }
    public void test(){
        Test.setBackgroundColor(0xfff00000);
    }

    //Methods for returning buttons color to the default white after the tap

    public void wletterQ(){
        LetterQ.setBackgroundColor(0x88888888);
    }
    public void wletterW(){
        LetterW.setBackgroundColor(0x88888888);
    }
    public void wletterE(){
        LetterE.setBackgroundColor(0x88888888);
    }
    public void wletterR(){
        LetterR.setBackgroundColor(0x88888888);
    }
    public void wletterT(){
        LetterT.setBackgroundColor(0x88888888);
    }
    public void wletterY(){
        LetterY.setBackgroundColor(0x88888888);
    }
    public void wletterU(){
        LetterU.setBackgroundColor(0x88888888);
    }
    public void wletterI(){
        LetterI.setBackgroundColor(0x88888888);
    }
    public void wletterO(){
        LetterO.setBackgroundColor(0x88888888);
    }
    public void wletterP(){
        LetterP.setBackgroundColor(0x88888888);
    }
    public void wletterA(){
        LetterA.setBackgroundColor(0x88888888);
    }
    public void wletterS(){
        LetterS.setBackgroundColor(0x88888888);
    }
    public void wletterD(){
        LetterD.setBackgroundColor(0x88888888);
    }
    public void wletterF(){
        LetterF.setBackgroundColor(0x88888888);
    }
    public void wletterG(){
        LetterG.setBackgroundColor(0x88888888);
    }
    public void wletterH(){
        LetterH.setBackgroundColor(0x88888888);
    }public void wletterJ(){
        LetterJ.setBackgroundColor(0x88888888);
    }public void wletterK(){
        LetterK.setBackgroundColor(0x88888888);
    }public void wletterL(){
        LetterL.setBackgroundColor(0x88888888);
    }public void wletterZ(){
        LetterZ.setBackgroundColor(0x88888888);
    }public void wletterX(){
        LetterX.setBackgroundColor(0x88888888);
    }
    public void wletterC(){
        LetterC.setBackgroundColor(0x88888888);
    }
    public void wletterV(){
        LetterV.setBackgroundColor(0x88888888);
    }
    public void wletterB(){
        LetterB.setBackgroundColor(0x88888888);
    }
    public void wletterN(){
        LetterN.setBackgroundColor(0x88888888);
    }
    public void wletterM(){
        LetterM.setBackgroundColor(0x88888888);
    }
    public void wSpace(){
        Space.setBackgroundColor(0x88888888);
    }
    public void wBackSpace(){
        Backspace.setBackgroundColor(0x88888888);
    }
    public void wTouch(){
        Touch.setBackgroundColor(0x88888888);
    }
    public void wMidAir(){
        MidAir.setBackgroundColor(0x88888888);
    }
    public void wTest(){
        Test.setBackgroundColor(0x88888888);
    }


    // Method for appending results for the Touch Text file

    public void writeTextTouch(String data){
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        StringBuilder text = new StringBuilder();
        dir.mkdirs();
        String filename = "TouchTest.txt";
        File file = new File(dir, filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method for appending results to the Mid Air text file
    public void writeTextAir(String data){
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        StringBuilder text = new StringBuilder();
        dir.mkdirs();
        String filename = "MidAirTest.txt";
        File file = new File(dir, filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method for creating Mid Air results text file
    public void writeTextabsAir(String data){
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        StringBuilder text = new StringBuilder();
        dir.mkdirs();
        String filename = "MidAirTest.txt";
        File file = new File(dir, filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method for creating Touch results text file
    public void writeTextabsTouch(String data){
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/Bachelor/");
        StringBuilder text = new StringBuilder();
        dir.mkdirs();
        String filename = "TouchTest.txt";
        File file = new File(dir, filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 6 Blocks where each block contains 15 sentences for Mid Air test
    public void airBlock1(){
        switch (midAirCounter) {
            case 1:
                textView14.setText("HELLO \n"); //The appearing sentence
                writeTextabsAir("BLOCK 1 ; " + "\n" + "\n"); // Creating and Writing to the test text file
                midAirCounter = 2; //Counter for sentences
                elapsedTimeAir = 0; //Resetting time
                airError = 0; // Resetting sentence error counter
                startTimeAir = System.nanoTime(); // starting new time
                break;
            case 2:
                if (textView14.getText().toString().equals("HELLO \nHELLO")) {
                    textView14.setText("HELLO WORLD \n");
                    midAirCounter = 3;
                    writeTextAir("HELLO : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError); //Appending to text file
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("HELLO \n");
                }
                break;
            case 3:
                if (textView14.getText().toString().equals("HELLO WORLD \nHELLO WORLD")) {
                    textView14.setText("RIGHT NOT LEFT \n");
                    writeTextAir("\n" + "\n" + "HELLO WORLD : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                    midAirCounter = 4;
                } else {
                    textView14.setText("HELLO WORLD \n");
                }
                break;
            case 4:
                if (textView14.getText().toString().equals("RIGHT NOT LEFT \nRIGHT NOT LEFT")) {
                    textView14.setText("I ATE APPLES \n");
                    writeTextAir("\n" + "\n" + "RIGHT NOT LEFT : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    midAirCounter = 5;
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                }
                else{
                    textView14.setText("RIGHT NOT LEFT \n");
                }
                break;
            case 5:
                if (textView14.getText().toString().equals("I ATE APPLES \nI ATE APPLES")) {
                    textView14.setText("I LOVE DONKEY \n");
                    writeTextAir("\n" + "I ATE APPLES : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    midAirCounter = 6;
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I ATE APPLES \n");
                }
                break;
            case 6:
                if (textView14.getText().toString().equals("I LOVE DONKEY \nI LOVE DONKEY")) {
                    textView14.setText("DELICIOUS \n");
                    midAirCounter = 7;
                    writeTextAir("\n" + "\n" + "I LOVE DONKEY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I LOVE DONKEY \n");
                }
                break;
            case 7:
                if (textView14.getText().toString().equals("DELICIOUS \nDELICIOUS")) {
                    textView14.setText("MADRID FANS \n");
                    midAirCounter = 8;
                    writeTextAir("\n" + "\n" + "DELICIOUS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("DELICIOUS \n");
                }
                break;
            case 8:
                if (textView14.getText().toString().equals("MADRID FANS \nMADRID FANS")) {
                    textView14.setText("THIS IS EGYPT \n");
                    midAirCounter = 9;
                    writeTextAir("\n" + "\n" + "MADRID FANS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MADRID FANS \n");
                }
                break;
            case 9:
                if (textView14.getText().toString().equals("THIS IS EGYPT \nTHIS IS EGYPT")) {
                    textView14.setText("PLAYSTATION CONSOLE \n");
                    midAirCounter = 10;
                    writeTextAir("\n" + "\n" + "THIS IS EGYPT : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THIS IS EGYPT \n");
                }
                break;
            case 10:
                if (textView14.getText().toString().equals("PLAYSTATION CONSOLE \nPLAYSTATION CONSOLE")) {
                    textView14.setText("CORONA VIRUS \n");
                    midAirCounter = 11;
                    writeTextAir("\n" + "\n" + "PLAYSTATION CONSOLE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("PLAYSTATION CONSOLE \n");
                }
                break;
            case 11:
                if (textView14.getText().toString().equals("CORONA VIRUS \nCORONA VIRUS")) {
                    textView14.setText("KEEP AT HOME \n");
                    midAirCounter = 12;
                    writeTextAir("\n" + "\n" + "CORONA VIRUS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("CORONA VIRUS \n");
                }
                break;
            case 12:
                if (textView14.getText().toString().equals("KEEP AT HOME \nKEEP AT HOME")) {
                    textView14.setText("JOE ABO ELMAKAREM \n");
                    midAirCounter = 13;
                    writeTextAir("\n" + "\n" + "KEEP AT HOME : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("KEEP AT HOME \n");
                }
                break;
            case 13:
                if (textView14.getText().toString().equals("JOE ABO ELMAKAREM \nJOE ABO ELMAKAREM")) {
                    textView14.setText("SEND A SNAP \n");
                    midAirCounter = 14;
                    writeTextAir("\n" + "\n" + "JOE ABO ELMAKAREM : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("JOE ABO ELMAKAREM \n");
                }
                break;
            case 14:
                if (textView14.getText().toString().equals("SEND A SNAP \nSEND A SNAP")) {
                    textView14.setText("MOSQUITO \n");
                    midAirCounter = 15;
                    writeTextAir("\n" + "\n" + "SEND A SNAP : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("SEND A SNAP \n");
                }
                break;
            case 15:
                if (textView14.getText().toString().equals("MOSQUITO \nMOSQUITO")) {
                    textView14.setText("HAPPY NEW YEAR \n");
                    midAirCounter = 16;
                    writeTextAir("\n" + "\n" + "MOSQUITO : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();

                } else {
                    textView14.setText("MOSQUITO \n");
                }
                break;
            case 16:
                if (textView14.getText().toString().equals("HAPPY NEW YEAR \nHAPPY NEW YEAR")) {
                    textView14.setText("BLOCK 1 DONE !! \n");
                    midAirCounter = 100;
                    writeTextAir("\n" + "\n" + "HAPPY NEW YEAR : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();

                } else {
                    textView14.setText("HAPPY NEW YEAR \n");
                }
                break;
            case 100:
                midAirCounter = 17;
                elapsedTimeAir = 0;
                airError = 0;
                midAirBlock = 2; //Switching to next block
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    public void airBlock2(){
        switch (midAirCounter) {

            case 17:
                textView14.setText("I HATE THIS APP");
                midAirCounter = 18;
                elapsedTimeAir = 0;
                airError = 0;
                startTimeAir = System.nanoTime();
                break;
            case 18:
                if (textView14.getText().toString().equals("I HATE THIS APP \nI HATE THIS APP")) {
                    textView14.setText("WINTER VIBES \n");
                    midAirCounter = 19;
                    writeTextAir("\n" + "\n" + "I HATE THIS APP : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();

                } else {
                    textView14.setText("I HATE THIS APP \n");
                }
                break;
            case 19:
                if (textView14.getText().toString().equals("WINTER VIBES \nWINTER VIBES")) {
                    textView14.setText("I AM A AUC STUDENT \n");
                    midAirCounter = 20;
                    writeTextAir("\n" + "\n" + "WINTER VIBES : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("WINTER VIBES \n");
                }
                break;
            case 20:
                if (textView14.getText().toString().equals("I AM A AUC STUDENT \nI AM A AUC STUDENT")) {
                    textView14.setText("COMPUTER PROGRAMMING \n");
                    midAirCounter = 21;
                    writeTextAir("\n" + "\n" + "I AM A AUC STUDENT : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I AM A AUC STUDENT \n");
                }
                break;
            case 21:
                if (textView14.getText().toString().equals("COMPUTER PROGRAMMING \nCOMPUTER PROGRAMMING")) {
                    textView14.setText("LET CLIENTS ON HOLD \n");
                    midAirCounter = 22;
                    writeTextAir("\n" + "\n" + "COMPUTER PROGRAMMING : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("COMPUTER PROGRAMMING \n");
                }
                break;
            case 22:
                if (textView14.getText().toString().equals("LET CLIENTS ON HOLD \nLET CLIENTS ON HOLD")) {
                    textView14.setText("MY FAVORITE COLOR IS YELLOW \n");
                    midAirCounter = 23;
                    writeTextAir("\n" + "\n" + "LET CLIENTS ON HOLD : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("LET CLIENTS ON HOLD \n");
                }
                break;
            case 23:
                if (textView14.getText().toString().equals("MY FAVORITE COLOR IS YELLOW \nMY FAVORITE COLOR IS YELLOW")) {
                    textView14.setText("CALL OF DUTY MODERN WAREFARE \n");
                    midAirCounter = 24;
                    writeTextAir("\n" + "\n" + "MY FAVORITE COLOR IS YELLOW : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MY FAVORITE COLOR IS YELLOW \n");
                }
                break;
            case 24:
                if (textView14.getText().toString().equals("CALL OF DUTY MODERN WAREFARE \nCALL OF DUTY MODERN WAREFARE")) {
                    textView14.setText("I LOVE CATS MORE THAN DOGS \n");
                    midAirCounter = 25;
                    writeTextAir("\n" + "\n" + "CALL OF DUTY MODERN WAREFARE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("CALL OF DUTY MODERN WAREFARE \n");
                }
                break;
            case 25:
                if (textView14.getText().toString().equals("I LOVE CATS MORE THAN DOGS \nI LOVE CATS MORE THAN DOGS")) {
                    textView14.setText("RAMADAN NIGHTS \n");
                    midAirCounter = 26;
                    writeTextAir("\n" + "\n" + "I LOVE CATS MORE THAN DOGS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I LOVE CATS MORE THAN DOGS \n");
                }
                break;
            case 26:
                if (textView14.getText().toString().equals("RAMADAN NIGHTS \nRAMADAN NIGHTS")) {
                    textView14.setText("ARCHITECTURE \n");
                    midAirCounter = 27;
                    writeTextAir("\n" + "\n" + "RAMADAN NIGHTS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("RAMADAN NIGHTS \n");
                }
                break;
            case 27:
                if (textView14.getText().toString().equals("ARCHITECTURE \nARCHITECTURE")) {
                    textView14.setText("WE ADORE PLAYING SOCCER \n");
                    midAirCounter = 28;
                    writeTextAir("\n" + "\n" + "ARCHITECTURE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("ARCHITECTURE \n");
                }
                break;
            case 28:
                if (textView14.getText().toString().equals("WE ADORE PLAYING SOCCER \nWE ADORE PLAYING SOCCER")) {
                    textView14.setText("YOUSSEF ABO EL MAKAREM \n");
                    midAirCounter = 29;
                    writeTextAir("\n" + "\n" + "WE ADORE PLAYING SOCCER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("WE ADORE PLAYING SOCCER \n");
                }
                break;
            case 29:
                if (textView14.getText().toString().equals("YOUSSEF ABO EL MAKAREM \nYOUSSEF ABO EL MAKAREM")) {
                    textView14.setText("THIS YEAR IS DARKED \n");
                    midAirCounter = 30;
                    writeTextAir("\n" + "\n" + "YOUSSEF ABO EL MAKAREM : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("YOUSSEF ABO EL MAKAREM \n");
                }
                break;
            case 30:
                if (textView14.getText().toString().equals("THIS YEAR IS DARKED \nTHIS YEAR IS DARKED")) {
                    textView14.setText("I CAME BACK FROM THE NORTHCOAST \n");
                    midAirCounter = 31;
                    writeTextAir("\n" + "\n" + "THIS YEAR IS DARKED : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THIS YEAR IS DARKED \n");
                }
                break;
            case 31:
                if (textView14.getText().toString().equals("I CAME BACK FROM THE NORTHCOAST \nI CAME BACK FROM THE NORTHCOAST")) {
                    textView14.setText("I HAVE A CAR \n");
                    midAirCounter = 32;
                    writeTextAir("\n" + "\n" + "I CAME BACK FROM THE NORTHCOAST : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I CAME BACK FROM THE NORTHCOAST \n");
                }
                break;
            case 32:
                if (textView14.getText().toString().equals("I HAVE A CAR \nI HAVE A CAR")) {
                    textView14.setText("BLOCK 2 IS DONE !! \n");
                    midAirCounter = 101;
                    writeTextAir("\n" + "\n" + "I HAVE A CAR : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I HAVE A CAR \n");
                }
                break;
            case 101:
                midAirCounter = 33;
                elapsedTimeAir = 0;
                airError = 0;
                midAirBlock = 2;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }


    public void airBLock3(){
        switch (midAirCounter){
            case 33:
                textView14.setText("I HATE WATCHING RAMADAN SERIES \n");
                midAirCounter = 34;
                elapsedTimeAir = 0;
                airError = 0;
                startTimeAir = System.nanoTime();
                break;
            case 34:
                if (textView14.getText().toString().equals("I HATE WATCHING RAMADAN SERIES \nI HATE WATCHING RAMADAN SERIES")) {
                    textView14.setText("MY CAT IS EIGHT MONTHS OLD \n");
                    midAirCounter = 35;
                    writeTextAir("\n" + "\n" + "I HATE WATCHING RAMADAN SERIES : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I HATE WATCHING RAMADAN SERIES \n");
                }
                break;
            case 35:
                if (textView14.getText().toString().equals("MY CAT IS EIGHT MONTHS OLD \nMY CAT IS EIGHT MONTHS OLD")) {
                    textView14.setText("THE INTERNET SPEED IS TOO FAST \n");
                    midAirCounter = 36;
                    writeTextAir("\n" + "\n" + "MY CAT IS EIGHT MONTHS OLD : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MY CAT IS EIGHT MONTHS OLD \n");
                }
                break;
            case 36:
                if (textView14.getText().toString().equals("THE INTERNET SPEED IS TOO FAST \nTHE INTERNET SPEED IS TOO FAST")) {
                    textView14.setText("DRINKING ALCOHOL IS RISKY \n");
                    midAirCounter = 37;
                    writeTextAir("\n" + "\n" + "THE INTERNET SPEED IS TOO FAST : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THE INTERNET SPEED IS TOO FAST \n");
                }
                break;
            case 37:
                if (textView14.getText().toString().equals("DRINKING ALCOHOL IS RISKY \nDRINKING ALCOHOL IS RISKY")) {
                    textView14.setText("COWS GIVES CHEESE \n");
                    midAirCounter = 38;
                    writeTextAir("\n" + "\n" + "DRINKING ALCOHOL IS RISKY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("DRINKING ALCOHOL IS RISKY \n");
                }
                break;
            case 38:
                if (textView14.getText().toString().equals("COWS GIVES CHEESE \nCOWS GIVES CHEESE")) {
                    textView14.setText("WE ARE MISSING FUSSBALL \n");
                    midAirCounter = 39;
                    writeTextAir("\n" + "\n" + "COWS GIVES CHEESE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("COWS GIVES CHEESE \n");
                }
                break;
            case 39:
                if (textView14.getText().toString().equals("WE ARE MISSING FUSSBALL \nWE ARE MISSING FUSSBALL")) {
                    textView14.setText("I PLAY FLUTE \n");
                    midAirCounter = 40;
                    writeTextAir("\n" + "\n" + "WE ARE MISSING FUSSBALL : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("WE ARE MISSING FUSSBALL \n");
                }
                break;
            case 40:
                if (textView14.getText().toString().equals("I PLAY FLUTE \nI PLAY FLUTE")) {
                    textView14.setText("MUSIC CURE WOUNDS \n");
                    midAirCounter = 41;
                    writeTextAir("\n" + "\n" + "I PLAY FLUTE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I PLAY FLUTE \n");
                }
                break;
            case 41:
                if (textView14.getText().toString().equals("MUSIC CURE WOUNDS \nMUSIC CURE WOUNDS")) {
                    textView14.setText("I CAN LIVE WITHOUT A PLAYSTATION \n");
                    midAirCounter = 42;
                    writeTextAir("\n" + "\n" + "MUSIC CURE WOUNDS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MUSIC CURE WOUNDS \n");
                }
                break;
            case 42:
                if (textView14.getText().toString().equals("I CAN LIVE WITHOUT A PLAYSTATION \nI CAN LIVE WITHOUT A PLAYSTATION")) {
                    textView14.setText("SUMMER IS BETTER THAN WINTER \n");
                    midAirCounter = 43;
                    writeTextAir("\n" + "\n" + "I CAN LIVE WITHOUT A PLAYSTATION : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I CAN LIVE WITHOUT A PLAYSTATION \n");
                }
                break;
            case 43:
                if (textView14.getText().toString().equals("SUMMER IS BETTER THAN WINTER \nSUMMER IS BETTER THAN WINTER")) {
                    textView14.setText("HAPPY EID \n");
                    midAirCounter = 44;
                    writeTextAir("\n" + "\n" + "SUMMER IS BETTER THAN WINTER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("SUMMER IS BETTER THAN WINTER \n");
                }
                break;
            case 44:
                if (textView14.getText().toString().equals("HAPPY EID \nHAPPY EID")) {
                    textView14.setText("IPHONE IS BETTER THAN ANDROID \n");
                    midAirCounter = 45;
                    writeTextAir("\n" + "\n" + "HAPPY EID : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("HAPPY EID \n");
                }
                break;
            case 45:
                if (textView14.getText().toString().equals("IPHONE IS BETTER THAN ANDROID \nIPHONE IS BETTER THAN ANDROID")) {
                    textView14.setText("AGRICULTURE \n");
                    midAirCounter = 46;
                    writeTextAir("\n" + "\n" + "IPHONE IS BETTER THAN ANDROID : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("IPHONE IS BETTER THAN ANDROID \n");
                }
                break;
            case 46:
                if (textView14.getText().toString().equals("AGRICULTURE \nAGRICULTURE")) {
                    textView14.setText("I HAD CAKE FOR BREAKFAST TODAY \n");
                    midAirCounter = 47;
                    writeTextAir("\n" + "\n" + "AGRICULTURE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("AGRICULTURE \n");
                }
                break;
            case 47:
                if (textView14.getText().toString().equals("I HAD CAKE FOR BREAKFAST TODAY \nI HAD CAKE FOR BREAKFAST TODAY")) {
                    textView14.setText("I LIKE GOING TO A BAR \n");
                    midAirCounter = 48;
                    writeTextAir("\n" + "\n" + "I HAD CAKE FOR BREAKFAST TODAY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                    midAirBlock = 4;
                } else {
                    textView14.setText("I HAD CAKE FOR BREAKFAST TODAY \n");
                }
                break;
            case 48:
                if (textView14.getText().toString().equals("I LIKE GOING TO A BAR \nI LIKE GOING TO A BAR")) {
                    textView14.setText("BLOCK 3 IS DONE !! \n");
                    midAirCounter = 102;
                    writeTextAir("\n" + "\n" + "I LIKE GOING TO A BAR : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I LIKE GOING TO A BAR \n");
                }
                break;
            case 102:
                midAirCounter = 49;
                elapsedTimeAir = 0;
                airError = 0;
                midAirBlock = 4;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    public void airBlock4(){
        switch (midAirCounter){
            case 49:
                textView14.setText("I ADORE SHARMOOFERS \n");
                midAirCounter = 50;
                elapsedTimeAir = 0;
                airError = 0;
                startTimeAir = System.nanoTime();

                break;
            case 50:
                if (textView14.getText().toString().equals("I ADORE SHARMOOFERS \nI ADORE SHARMOOFERS")) {
                    textView14.setText("MY CAT BITES \n");
                    midAirCounter = 51;
                    writeTextAir("\n" + "\n" + "I ADORE SHARMOOFERS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I ADORE SHARMOOFERS \n");
                }
                break;
            case 51:
                if (textView14.getText().toString().equals("MY CAT BITES \nMY CAT BITES")) {
                    textView14.setText("BACHELOR \n");
                    midAirCounter = 52;
                    writeTextAir("\n" + "\n" + "MY CAT BITES : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MY CAT BITES \n");
                }
                break;
            case 52:
                if (textView14.getText().toString().equals("BACHELOR \nBACHELOR")) {
                    textView14.setText("AMWAJ \n");
                    midAirCounter = 53;
                    writeTextAir("\n" + "\n" + "BACHELOR : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("BACHELOR \n");
                }
                break;
            case 53:
                if (textView14.getText().toString().equals("AMWAJ \nAMWAJ")) {
                    textView14.setText("WASH YOUR HANDS USUALLY \n");
                    midAirCounter = 54;
                    writeTextAir("\n" + "\n" + "AMWAJ : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("AMWAJ \n");
                }
                break;
            case 54:
                if (textView14.getText().toString().equals("WASH YOUR HANDS USUALLY \nWASH YOUR HANDS USUALLY")) {
                    textView14.setText("WASH YOUR TEETH \n");
                    midAirCounter = 55;
                    writeTextAir("\n" + "\n" + "WASH YOUR HANDS USUALLY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("WASH YOUR HANDS USUALLY \n");
                }
                break;
            case 55:
                if (textView14.getText().toString().equals("WASH YOUR TEETH \nWASH YOUR TEETH")) {
                    textView14.setText("MY FAVORITE COLOR IS GREY \n");
                    midAirCounter = 56;
                    writeTextAir("\n" + "\n" + "WASH YOUR TEETH : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("WASH YOUR TEETH \n");
                }
                break;
            case 56:
                if (textView14.getText().toString().equals("MY FAVORITE COLOR IS GREY \nMY FAVORITE COLOR IS GREY")) {
                    textView14.setText("I WANT TO GO TO THE BALLROOM \n");
                    midAirCounter = 57;
                    writeTextAir("\n" + "\n" + "MY FAVORITE COLOR IS GREY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MY FAVORITE COLOR IS GREY \n");
                }
                break;
            case 57:
                if (textView14.getText().toString().equals("I WANT TO GO TO THE BALLROOM \nI WANT TO GO TO THE BALLROOM")) {
                    textView14.setText("THIS CAT IS SO CUTE \n");
                    midAirCounter = 58;
                    writeTextAir("\n" + "\n" + "I WANT TO GO TO THE BALLROOM : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I WANT TO GO TO THE BALLROOM \n");
                }
                break;
            case 58:
                if (textView14.getText().toString().equals("THIS CAT IS SO CUTE \nTHIS CAT IS SO CUTE")) {
                    textView14.setText("I WANT TO SEE MY PARTNER \n");
                    midAirCounter = 59;
                    writeTextAir("\n" + "\n" + "THIS CAT IS SO CUTE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THIS CAT IS SO CUTE \n");
                }
                break;
            case 59:
                if (textView14.getText().toString().equals("I WANT TO SEE MY PARTNER \nI WANT TO SEE MY PARTNER")) {
                    textView14.setText("RONALDO OR MESSI \n");
                    midAirCounter = 60;
                    writeTextAir("\n" + "\n" + "I WANT TO SEE MY PARTNER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I WANT TO SEE MY PARTNER \n");
                }
                break;
            case 60:
                if (textView14.getText().toString().equals("RONALDO OR MESSI \nRONALDO OR MESSI")) {
                    textView14.setText("I AM GOOD BLESS YOU \n");
                    midAirCounter = 61;
                    writeTextAir("\n" + "\n" + "RONALDO OR MESSI : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("RONALDO OR MESSI \n");
                }
                break;
            case 61:
                if (textView14.getText().toString().equals("I AM GOOD BLESS YOU \nI AM GOOD BLESS YOU")) {
                    textView14.setText("I WENT OUT YESTERDAY \n");
                    midAirCounter = 62;
                    writeTextAir("\n" + "\n" + "I AM GOOD BLESS YOU : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I AM GOOD BLESS YOU \n");
                }
                break;
            case 62:
                if (textView14.getText().toString().equals("I WENT OUT YESTERDAY \nI WENT OUT YESTERDAY")) {
                    textView14.setText("I LOVE YOUR BRAIDS \n");
                    midAirCounter = 63;
                    writeTextAir("\n" + "\n" + "I WENT OUT YESTERDAY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I WENT OUT YESTERDAY \n");
                }
                break;
            case 63:
                if (textView14.getText().toString().equals("I LOVE YOUR BRAIDS \nI LOVE YOUR BRAIDS")) {
                    textView14.setText("I PREFER SAMSUNG OVER APPLE \n");
                    midAirCounter = 64;
                    writeTextAir("\n" + "\n" + "I LOVE YOUR BRAIDS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I LOVE YOUR BRAIDS \n");
                }
                break;
            case 64:
                if (textView14.getText().toString().equals("I PREFER SAMSUNG OVER APPLE \nI PREFER SAMSUNG OVER APPLE")) {
                    textView14.setText("BLOCK 4 IS DONE !! \n");
                    midAirCounter = 103;
                    writeTextAir("\n" + "\n" + "I PREFER SAMSUNG OVER APPLE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I PREFER SAMSUNG OVER APPLE \n");
                }
                break;
            case 103:
                midAirCounter = 65;
                elapsedTimeAir = 0;
                airError = 0;
                midAirBlock = 2;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    public void airBlock5(){
        switch (midAirCounter){
            case 65:
                textView14.setText("I PREFER ADIDAS OVER NIKE");
                midAirCounter = 66;
                elapsedTimeAir = 0;
                airError = 0;
                startTimeAir = System.nanoTime();

                break;
            case 66:
                if (textView14.getText().toString().equals("I PREFER ADIDAS OVER NIKE \nI PREFER ADIDAS OVER NIKE")) {
                    textView14.setText("I HATE PLAYING SQUASH \n");
                    midAirCounter = 67;
                    writeTextAir("\n" + "\n" + "I PREFER ADIDAS OVER NIKE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I PREFER ADIDAS OVER NIKE \n");
                }
                break;
            case 67:
                if (textView14.getText().toString().equals("I HATE PLAYING SQUASH \nI HATE PLAYING SQUASH")) {
                    textView14.setText("I AM AFRAID TO DIE \n");
                    midAirCounter = 68;
                    writeTextAir("\n" + "\n" + "I HATE PLAYING SQUASH : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I HATE PLAYING SQUASH \n");
                }
                break;
            case 68:
                if (textView14.getText().toString().equals("I AM AFRAID TO DIE \nI AM AFRAID TO DIE")) {
                    textView14.setText("YOU HAVE TO SAY HELLO \n");
                    midAirCounter = 69;
                    writeTextAir("\n" + "\n" + "I AM AFRAID TO DIE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I AM AFRAID TO DIE \n");
                }
                break;
            case 69:
                if (textView14.getText().toString().equals("YOU HAVE TO SAY HELLO \nYOU HAVE TO SAY HELLO")) {
                    textView14.setText("I LOVE YOU I HATE YOU \n");
                    midAirCounter = 70;
                    writeTextAir("\n" + "\n" + "YOU HAVE TO SAY HELLO : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("YOU HAVE TO SAY HELLO \n");
                }
                break;
            case 70:
                if (textView14.getText().toString().equals("I LOVE YOU I HATE YOU \nI LOVE YOU I HATE YOU")) {
                    textView14.setText("I HATE HIM SO MUCH \n");
                    midAirCounter = 71;
                    writeTextAir("\n" + "\n" + "I LOVE YOU I HATE YOU : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I LOVE YOU I HATE YOU \n");
                }
                break;
            case 71:
                if (textView14.getText().toString().equals("I HATE HIM SO MUCH \nI HATE HIM SO MUCH")) {
                    textView14.setText("I NEED TO FIND MY CAR \n");
                    midAirCounter = 72;
                    writeTextAir("\n" + "\n" + "I HATE HIM SO MUCH : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I HATE HIM SO MUCH \n");
                }
                break;
            case 72:
                if (textView14.getText().toString().equals("I NEED TO FIND MY CAR \nI NEED TO FIND MY CAR")) {
                    textView14.setText("THIS HOUSE NEEDS REDECORATION \n");
                    midAirCounter = 73;
                    writeTextAir("\n" + "\n" + "I NEED TO FIND MY CAR : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I NEED TO FIND MY CAR \n");
                }
                break;
            case 73:
                if (textView14.getText().toString().equals("THIS HOUSE NEEDS REDECORATION \nTHIS HOUSE NEEDS REDECORATION")) {
                    textView14.setText("CORONAVIRUS IS FRIGHTENING \n");
                    midAirCounter = 74;
                    writeTextAir("\n" + "\n" + "THIS HOUSE NEEDS REDECORATION : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THIS HOUSE NEEDS REDECORATION \n");
                }
                break;
            case 74:
                if (textView14.getText().toString().equals("CORONAVIRUS IS FRIGHTENING \nCORONAVIRUS IS FRIGHTENING")) {
                    textView14.setText("MY MOBILE IS RUNNING OUT OF BATTERY \n");
                    midAirCounter = 75;
                    writeTextAir("\n" + "\n" + "CORONAVIRUS IS FRIGHTENING : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("CORONAVIRUS IS FRIGHTENING \n");
                }
                break;
            case 75:
                if (textView14.getText().toString().equals("MY MOBILE IS RUNNING OUT OF BATTERY \nMY MOBILE IS RUNNING OUT OF BATTERY")) {
                    textView14.setText("NO WOMAN NO CRY \n");
                    midAirCounter = 76;
                    writeTextAir("\n" + "\n" + "MY MOBILE IS RUNNING OUT OF BATTERY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("MY MOBILE IS RUNNING OUT OF BATTERY \n");
                }
                break;
            case 76:
                if (textView14.getText().toString().equals("NO WOMAN NO CRY \nNO WOMAN NO CRY")) {
                    textView14.setText("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \n");
                    midAirCounter = 77;
                    writeTextAir("\n" + "\n" + "NO WOMAN NO CRY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("NO WOMAN NO CRY \n");
                }
                break;
            case 77:
                if (textView14.getText().toString().equals("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \nNOW AND THEN I THINK OF WHEN WE WERE TOGETHER")) {
                    textView14.setText("I FEEL TOO SCARED \n");
                    midAirCounter = 78;
                    writeTextAir("\n" + "\n" + "NOW AND THEN I THINK OF WHEN WE WERE TOGETHER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \n");
                }
                break;
            case 78:
                if (textView14.getText().toString().equals("I FEEL TOO SCARED \nI FEEL TOO SCARED")) {
                    textView14.setText("I SET FIRE TO A SCHOOL \n");
                    midAirCounter = 79;
                    writeTextAir("\n" + "\n" + "I FEEL TOO SCARED : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I FEEL TOO SCARED \n");
                }
                break;
            case 79:
                if (textView14.getText().toString().equals("I SET FIRE TO A SCHOOL \nI SET FIRE TO A SCHOOL")) {
                    textView14.setText("SADNESS IS KEEPING ME BACK \n");
                    midAirCounter = 80;
                    writeTextAir("\n" + "\n" + "I SET FIRE TO A SCHOOL : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I SET FIRE TO A SCHOOL \n");
                }
                break;
            case 80:
                if (textView14.getText().toString().equals("SADNESS IS KEEPING ME BACK \nSADNESS IS KEEPING ME BACK")) {
                    textView14.setText("BLOCK 5 IS DONE !! \n");
                    midAirCounter = 104;
                    writeTextAir("\n" + "\n" + "SADNESS IS KEEPING ME BACK : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("SADNESS IS KEEPING ME BACK \n");
                }
                break;
            case 104:
                midAirCounter = 82;
                elapsedTimeAir = 0;
                airError = 0;
                midAirBlock = 2;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    public void airBlock6(){
        switch (midAirCounter){
            case 81:
                textView14.setText("SWEET DREAMS ARE MADE OF THIS");
                midAirCounter = 82;
                elapsedTimeAir = 0;
                airError = 0;
                startTimeAir = System.nanoTime();

                break;
            case 82:
                if (textView14.getText().toString().equals("SWEET DREAMS ARE MADE OF THIS \nSWEET DREAMS ARE MADE OF THIS")) {
                    textView14.setText("IT IS TOO LATE TO SAY SORRY \n");
                    midAirCounter = 83;
                    writeTextAir("\n" + "\n" + "SWEET DREAMS ARE MADE OF THIS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("SWEET DREAMS ARE MADE OF THIS \n");
                }
                break;
            case 83:
                if (textView14.getText().toString().equals("IT IS TOO LATE TO SAY SORRY \nIT IS TOO LATE TO SAY SORRY")) {
                    textView14.setText("THE WEATHER FEELS FREEZ \n");
                    midAirCounter = 84;
                    writeTextAir("\n" + "\n" + "IT IS TOO LATE TO SAY SORRY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("IT IS TOO LATE TO SAY SORRY \n");
                }
                break;
            case 84:
                if (textView14.getText().toString().equals("THE WEATHER FEELS FREEZ \nTHE WEATHER FEELS FREEZ")) {
                    textView14.setText("GOOD BOY \n");
                    midAirCounter = 85;
                    writeTextAir("\n" + "\n" + "THE WEATHER FEELS FREEZ : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THE WEATHER FEELS FREEZ \n");
                }
                break;
            case 85:
                if (textView14.getText().toString().equals("GOOD BOY \nGOOD BOY")) {
                    textView14.setText("KENTUCKY FRIED CHICKEN \n");
                    midAirCounter = 86;
                    writeTextAir("\n" + "\n" + "GOOD BOY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("GOOD BOY \n");
                }
                break;
            case 86:
                if (textView14.getText().toString().equals("KENTUCKY FRIED CHICKEN \nKENTUCKY FRIED CHICKEN")) {
                    textView14.setText("THE GERMAN UNIVERSITY IN CAIRO \n");
                    midAirCounter = 87;
                    writeTextAir("\n" + "\n" + "KENTUCKY FRIED CHICKEN : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("KENTUCKY FRIED CHICKEN \n");
                }
                break;
            case 87:
                if (textView14.getText().toString().equals("THE GERMAN UNIVERSITY IN CAIRO \nTHE GERMAN UNIVERSITY IN CAIRO")) {
                    textView14.setText("BLACK LIVES MATTER \n");
                    midAirCounter = 88;
                    writeTextAir("\n" + "\n" + "THE GERMAN UNIVERSITY IN CAIRO : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THE GERMAN UNIVERSITY IN CAIRO \n");
                }
                break;
            case 88:
                if (textView14.getText().toString().equals("BLACK LIVES MATTER \nBLACK LIVES MATTER")) {
                    textView14.setText("SLOW AND FURIOUS \n");
                    midAirCounter = 89;
                    writeTextAir("\n" + "\n" + "BLACK LIVES MATTER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("BLACK LIVES MATTER \n");
                }
                break;
            case 89:
                if (textView14.getText().toString().equals("SLOW AND FURIOUS \nSLOW AND FURIOUS")) {
                    textView14.setText("HARRY POTTER \n");
                    midAirCounter = 90;
                    writeTextAir("\n" + "\n" + "SLOW AND FURIOUS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("SLOW AND FURIOUS \n");
                }
                break;
            case 90:
                if (textView14.getText().toString().equals("HARRY POTTER \nHARRY POTTER")) {
                    textView14.setText("DARK HOLE \n");
                    midAirCounter = 91;
                    writeTextAir("\n" + "\n" + "HARRY POTTER : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("HARRY POTTER \n");
                }
                break;
            case 91:
                if (textView14.getText().toString().equals("DARK HOLE \nDARK HOLE")) {
                    textView14.setText("THESE SONGS MAKE ME FEEL SAD \n");
                    midAirCounter = 92;
                    writeTextAir("\n" + "\n" + "DARK HOLE : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("DARK HOLE \n");
                }
                break;
            case 92:
                if (textView14.getText().toString().equals("THESE SONGS MAKE ME FEEL SAD \nTHESE SONGS MAKE ME FEEL SAD")) {
                    textView14.setText("RAP GOD \n");
                    midAirCounter = 93;
                    writeTextAir("\n" + "\n" + "THESE SONGS MAKE ME FEEL SAD : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("THESE SONGS MAKE ME FEEL SAD \n");
                }
                break;
            case 93:
                if (textView14.getText().toString().equals("RAP GOD \nRAP GOD")) {
                    textView14.setText("I HATE WATHCING STARS \n");
                    midAirCounter = 94;
                    writeTextAir("\n" + "\n" + "RAP GOD : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("RAP GOD \n");
                }
                break;
            case 94:
                if (textView14.getText().toString().equals("I HATE WATHCING STARS \nI HATE WATHCING STARS")) {
                    textView14.setText("DARKNESS IS SCARY \n");
                    midAirCounter = 95;
                    writeTextAir("\n" + "\n" + "I HATE WATHCING STARS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("I HATE WATHCING STARS \n");
                }
                break;
            case 95:
                if (textView14.getText().toString().equals("DARKNESS IS SCARY \nDARKNESS IS SCARY")) {
                    textView14.setText("ALL MY FRIENDS ARE HEATHENS \n");
                    midAirCounter = 96;
                    writeTextAir("\n" + "\n" + "DARKNESS IS SCARY : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("DARKNESS IS SCARY \n");
                }
                break;
            case 96:
                if (textView14.getText().toString().equals("ALL MY FRIENDS ARE HEATHENS \nALL MY FRIENDS ARE HEATHENS")) {
                    textView14.setText("BLOCK 6 IS DONE !! \n");
                    midAirCounter = 105;
                    writeTextAir("\n" + "\n" + "ALL MY FRIENDS ARE HEATHENS : " + "\n" + "Air Elapsed Time : " + elapsedTimeAir + " s" + "\n" + "Air Errors : " + airError);
                    elapsedTimeAir = 0;
                    airError = 0;
                    startTimeAir = System.nanoTime();
                } else {
                    textView14.setText("ALL MY FRIENDS ARE HEATHENS \n");
                }
                break;
            case 105:
                elapsedTimeAir = 0;
                airError = 0;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    // 6 Blocks where each block contains 15 sentences for Touch test
    public void touchBlock1(){
        switch (touchCounter) {
            case 1:
                textView14.setText("HELLO \n");
                writeTextabsTouch("BLOCK 1 ; " + "\n" + "\n");
                touchCounter = 2;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();
                break;
            case 2:
                if (textView14.getText().toString().equals("HELLO \nHELLO")) {
                    textView14.setText("HELLO WORLD \n");
                    touchCounter = 3;
                    writeTextTouch("HELLO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("HELLO \n");
                }
                break;
            case 3:
                if (textView14.getText().toString().equals("HELLO WORLD \nHELLO WORLD")) {
                    textView14.setText("TIME IS MONEY \n");
                    writeTextTouch("\n" + "\n" + "HELLO WORLD : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    touchCounter = 4;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("HELLO WORLD \n");
                }
                break;
            case 4:
                if (textView14.getText().toString().equals("TIME IS MONEY \nTIME IS MONEY")) {
                    textView14.setText("I ATE BANANA \n");
                    writeTextTouch("\n" + "\n" + "TIME IS MONEY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    touchCounter = 5;
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                }
                else{
                    textView14.setText("TIME IS MONEY \n");
                }
                break;
            case 5:
                if (textView14.getText().toString().equals("I ATE BANANA \nI ATE BANANA")) {
                    textView14.setText("I AM A MONKEY \n");
                    writeTextTouch("\n"+"\n" + "I ATE BANANA : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    touchCounter = 6;
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I ATE BANANA \n");
                }
                break;
            case 6:
                if (textView14.getText().toString().equals("I AM A MONKEY \nI AM A MONKEY")) {
                    textView14.setText("BEAUTIFUL \n");
                    touchCounter = 7;
                    writeTextTouch("\n" + "\n" + "I AM A MONKEY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I AM A MONKEY \n");
                }
                break;
            case 7:
                if (textView14.getText().toString().equals("BEAUTIFUL \nBEAUTIFUL")) {
                    textView14.setText("REAL MADRID \n");
                    touchCounter = 8;
                    writeTextTouch("\n" + "\n" + "BEAUTIFUL : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("BEAUTIFUL \n");
                }
                break;
            case 8:
                if (textView14.getText().toString().equals("REAL MADRID \nREAL MADRID")) {
                    textView14.setText("THIS IS EGYPT \n");
                    touchCounter = 9;
                    writeTextTouch("\n" + "\n" + "REAL MADRID : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("REAL MADRID \n");
                }
                break;
            case 9:
                if (textView14.getText().toString().equals("THIS IS EGYPT \nTHIS IS EGYPT")) {
                    textView14.setText("PLAYSTATION NETWORK \n");
                    touchCounter = 10;
                    writeTextTouch("\n" + "\n" + "THIS IS EGYPT : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THIS IS EGYPT \n");
                }
                break;
            case 10:
                if (textView14.getText().toString().equals("PLAYSTATION NETWORK \nPLAYSTATION NETWORK")) {
                    textView14.setText("CORONA VIRUS \n");
                    touchCounter = 11;
                    writeTextTouch("\n" + "\n" + "PLAYSTATION NETWORK : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("PLAYSTATION NETWORK \n");
                }
                break;
            case 11:
                if (textView14.getText().toString().equals("CORONA VIRUS \nCORONA VIRUS")) {
                    textView14.setText("STAY AT HOME \n");
                    touchCounter = 12;
                    writeTextTouch("\n" + "\n" + "CORONA VIRUS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("CORONA VIRUS \n");
                }
                break;
            case 12:
                if (textView14.getText().toString().equals("STAY AT HOME \nSTAY AT HOME")) {
                    textView14.setText("CRISTIANO RONALDO \n");
                    touchCounter = 13;
                    writeTextTouch("\n" + "\n" + "STAY AT HOME : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("STAY AT HOME \n");
                }
                break;
            case 13:
                if (textView14.getText().toString().equals("CRISTIANO RONALDO \nCRISTIANO RONALDO")) {
                    textView14.setText("HOUSE PARTY \n");
                    touchCounter = 14;
                    writeTextTouch("\n" + "\n" + "CRISTIANO RONALDO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("CRISTIANO RONALDO \n");
                }
                break;
            case 14:
                if (textView14.getText().toString().equals("HOUSE PARTY \nHOUSE PARTY")) {
                    textView14.setText("MOSQUITO \n");
                    touchCounter = 15;
                    writeTextTouch("\n" + "\n" + "HOUSE PARTY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("HOUSE PARTY \n");
                }
                break;
            case 15:
                if (textView14.getText().toString().equals("MOSQUITO \nMOSQUITO")) {
                    textView14.setText("GET WELL SOON \n");
                    touchCounter = 16;
                    writeTextTouch("\n" + "\n" + "MOSQUITO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;

                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MOSQUITO \n");
                }
                break;
            case 16:
                if (textView14.getText().toString().equals("GET WELL SOON \nGET WELL SOON")) {
                    touchCounter = 100;
                    textView14.setText("BLOCK 1 DONE !! \n");
                    writeTextTouch("\n" + "\n" + "GET WELL SOON : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;

                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("GET WELL SOON \n");
                }
                break;
            case 100:

                elapsedTimeTouch = 0;
                touchError = 0;
                touchBlock = 2;
                touchCounter = 17;
                break;
            default:
                textView14.setText("ENDD \n");
        }
    }

    public void touchBlock2(){
        switch (touchCounter) {
            case 17:
                textView14.setText("I LOVE THIS APP");
                touchCounter = 18;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();
                break;
            case 18:
                if (textView14.getText().toString().equals("I LOVE THIS APP \nI LOVE THIS APP")) {
                    textView14.setText("SUMMER VIBES \n");
                    touchCounter = 19;
                    writeTextTouch("\n" + "\n" + "I LOVE THIS APP : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE THIS APP \n");
                }
                break;
            case 19:
                if (textView14.getText().toString().equals("SUMMER VIBES \nSUMMER VIBES")) {
                    textView14.setText("I AM A GUC STUDENT \n");
                    touchCounter = 20;
                    writeTextTouch("\n" + "\n" + "SUMMER VIBES : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("SUMMER VIBES \n");
                }
                break;
            case 20:
                if (textView14.getText().toString().equals("I AM A GUC STUDENT \nI AM A GUC STUDENT")) {
                    textView14.setText("COMPUTER ENGINEERING \n");
                    touchCounter = 21;
                    writeTextTouch("\n" + "\n" + "I AM A GUC STUDENT : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I AM A GUC STUDENT \n");
                }
                break;
            case 21:
                if (textView14.getText().toString().equals("COMPUTER ENGINEERING \nCOMPUTER ENGINEERING")) {
                    textView14.setText("THE WEATHER IS COLD \n");
                    touchCounter = 22;
                    writeTextTouch("\n" + "\n" + "COMPUTER ENGINEERING : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("COMPUTER ENGINEERING \n");
                }
                break;
            case 22:
                if (textView14.getText().toString().equals("THE WEATHER IS COLD \nTHE WEATHER IS COLD")) {
                    textView14.setText("MY FAVORITE HOBBY IS DRAWING \n");
                    touchCounter = 23;
                    writeTextTouch("\n" + "\n" + "THE WEATHER IS COLD : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THE WEATHER IS COLD \n");
                }
                break;
            case 23:
                if (textView14.getText().toString().equals("MY FAVORITE HOBBY IS DRAWING \nMY FAVORITE HOBBY IS DRAWING")) {
                    textView14.setText("CALL OF DUTY MODERN WAREFARE \n");
                    touchCounter = 24;
                    writeTextTouch("\n" + "\n" + "MY FAVORITE HOBBY IS DRAWING : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY FAVORITE HOBBY IS DRAWING \n");
                }
                break;
            case 24:
                if (textView14.getText().toString().equals("CALL OF DUTY MODERN WAREFARE \nCALL OF DUTY MODERN WAREFARE")) {
                    textView14.setText("I LOVE DOGS MORE THAN CATS \n");
                    touchCounter = 25;
                    writeTextTouch("\n" + "\n" + "CALL OF DUTY MODERN WAREFARE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("CALL OF DUTY MODERN WAREFARE \n");
                }
                break;
            case 25:
                if (textView14.getText().toString().equals("I LOVE DOGS MORE THAN CATS \nI LOVE DOGS MORE THAN CATS")) {
                    textView14.setText("RAMADAN KAREEM \n");
                    touchCounter = 26;
                    writeTextTouch("\n" + "\n" + "I LOVE DOGS MORE THAN CATS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE DOGS MORE THAN CATS \n");
                }
                break;
            case 26:
                if (textView14.getText().toString().equals("RAMADAN KAREEM \nRAMADAN KAREEM")) {
                    textView14.setText("ARCHITECTURE \n");
                    touchCounter = 27;
                    writeTextTouch("\n" + "\n" + "RAMADAN KAREEM : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("RAMADAN KAREEM \n");
                }
                break;
            case 27:
                if (textView14.getText().toString().equals("ARCHITECTURE \nARCHITECTURE")) {
                    textView14.setText("I LOVE PLAYING FOOTBALL \n");
                    touchCounter = 28;
                    writeTextTouch("\n" + "\n" + "ARCHITECTURE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("ARCHITECTURE \n");
                }
                break;
            case 28:
                if (textView14.getText().toString().equals("I LOVE PLAYING FOOTBALL \nI LOVE PLAYING FOOTBALL")) {
                    textView14.setText("YOUSSEF ABO EL MAKAREM \n");
                    touchCounter = 29;
                    writeTextTouch("\n" + "\n" + "I LOVE PLAYING FOOTBALL : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE PLAYING FOOTBALL \n");
                }
                break;
            case 29:
                if (textView14.getText().toString().equals("YOUSSEF ABO EL MAKAREM \nYOUSSEF ABO EL MAKAREM")) {
                    textView14.setText("THIS YEAR IS CURSED \n");
                    touchCounter = 30;
                    writeTextTouch("\n" + "\n" + "YOUSSEF ABO EL MAKAREM : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("YOUSSEF ABO EL MAKAREM \n");
                }
                break;
            case 30:
                if (textView14.getText().toString().equals("THIS YEAR IS CURSED \nTHIS YEAR IS CURSED")) {
                    textView14.setText("I WILL TRAVEL TO THE NORTHCOAST \n");
                    touchCounter = 31;
                    writeTextTouch("\n" + "\n" + "THIS YEAR IS CURSED : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THIS YEAR IS CURSED \n");
                }
                break;
            case 31:
                if (textView14.getText().toString().equals("I WILL TRAVEL TO THE NORTHCOAST \nI WILL TRAVEL TO THE NORTHCOAST")) {
                    textView14.setText("I OWN A BIKE \n");
                    touchCounter = 32;
                    writeTextTouch("\n" + "\n" + "I WILL TRAVEL TO THE NORTHCOAST : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I WILL TRAVEL TO THE NORTHCOAST \n");
                }
                break;
            case 32:
                if (textView14.getText().toString().equals("I OWN A BIKE \nI OWN A BIKE")) {
                    textView14.setText("BLOCK 2 IS DONE !! \n");
                    touchCounter = 101;
                    writeTextTouch("\n" + "\n" + "I OWN A BIKE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I OWN A BIKE \n");
                }
                break;
            case 101:
                touchError = 0;
                touchCounter = 33;
                touchBlock = 3;
                elapsedTimeTouch = 0;
                break;

            default:
                textView14.setText("ENDD \n");
        }
    }

    public void touchBlock3(){
        switch (touchCounter){
            case 33:
                textView14.setText("I LOVE WATCHING RAMADAN SERIES \n");
                touchCounter = 34;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();
                break;
            case 34:
                if (textView14.getText().toString().equals("I LOVE WATCHING RAMADAN SERIES \nI LOVE WATCHING RAMADAN SERIES")) {
                    textView14.setText("MY PUPPY IS SIX MONTHS OLD \n");
                    touchCounter = 35;
                    writeTextTouch("\n" + "\n" + "I LOVE WATCHING RAMADAN SERIES : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE WATCHING RAMADAN SERIES \n");
                }
                break;
            case 35:
                if (textView14.getText().toString().equals("MY PUPPY IS SIX MONTHS OLD \nMY PUPPY IS SIX MONTHS OLD")) {
                    textView14.setText("THE INTERNET SPEED IS TOO SLOW \n");
                    touchCounter = 36;
                    writeTextTouch("\n" + "\n" + "MY PUPPY IS SIX MONTHS OLD : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY PUPPY IS SIX MONTHS OLD \n");
                }
                break;
            case 36:
                if (textView14.getText().toString().equals("THE INTERNET SPEED IS TOO SLOW \nTHE INTERNET SPEED IS TOO SLOW")) {
                    textView14.setText("DRINKING WATER IS HEALTHY \n");
                    touchCounter = 37;
                    writeTextTouch("\n" + "\n" + "THE INTERNET SPEED IS TOO SLOW : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THE INTERNET SPEED IS TOO SLOW \n");
                }
                break;
            case 37:
                if (textView14.getText().toString().equals("DRINKING WATER IS HEALTHY \nDRINKING WATER IS HEALTHY")) {
                    textView14.setText("COWS PRODUCE MILK \n");
                    touchCounter = 38;
                    writeTextTouch("\n" + "\n" + "DRINKING WATER IS HEALTHY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("DRINKING WATER IS HEALTHY \n");
                }
                break;
            case 38:
                if (textView14.getText().toString().equals("COWS PRODUCE MILK \nCOWS PRODUCE MILK")) {
                    textView14.setText("WE ARE MISSING FOOTBALL \n");
                    touchCounter = 39;
                    writeTextTouch("\n" + "\n" + "COWS PRODUCE MILK : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("COWS PRODUCE MILK \n");
                }
                break;
            case 39:
                if (textView14.getText().toString().equals("WE ARE MISSING FOOTBALL \nWE ARE MISSING FOOTBALL")) {
                    textView14.setText("I PLAY DRUMS \n");
                    touchCounter = 40;
                    writeTextTouch("\n" + "\n" + "WE ARE MISSING FOOTBALL : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("WE ARE MISSING FOOTBALL \n");
                }
                break;
            case 40:
                if (textView14.getText().toString().equals("I PLAY DRUMS \nI PLAY DRUMS")) {
                    textView14.setText("MUSIC HEALS SOULS \n");
                    touchCounter = 41;
                    writeTextTouch("\n" + "\n" + "I PLAY DRUMS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I PLAY DRUMS \n");
                }
                break;
            case 41:
                if (textView14.getText().toString().equals("MUSIC HEALS SOULS \nMUSIC HEALS SOULS")) {
                    textView14.setText("I CANT LIVE WITHOUT MY CELLPHONE \n");
                    touchCounter = 42;
                    writeTextTouch("\n" + "\n" + "MUSIC HEALS SOULS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MUSIC HEALS SOULS \n");
                }
                break;
            case 42:
                if (textView14.getText().toString().equals("I CANT LIVE WITHOUT MY CELLPHONE \nI CANT LIVE WITHOUT MY CELLPHONE")) {
                    textView14.setText("WINTER IS BETTER THAN SUMMER \n");
                    touchCounter = 43;
                    writeTextTouch("\n" + "\n" + "I CANT LIVE WITHOUT MY CELLPHONE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I CANT LIVE WITHOUT MY CELLPHONE \n");
                }
                break;
            case 43:
                if (textView14.getText().toString().equals("WINTER IS BETTER THAN SUMMER \nWINTER IS BETTER THAN SUMMER")) {
                    textView14.setText("HAPPY EID \n");
                    touchCounter = 44;
                    writeTextTouch("\n" + "\n" + "WINTER IS BETTER THAN SUMMER : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("WINTER IS BETTER THAN SUMMER \n");
                }
                break;
            case 44:
                if (textView14.getText().toString().equals("HAPPY EID \nHAPPY EID")) {
                    textView14.setText("ANDROID IS BETTER THAN IPHONE \n");
                    touchCounter = 45;
                    writeTextTouch("\n" + "\n" + "HAPPY EID : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("HAPPY EID \n");
                }
                break;
            case 45:
                if (textView14.getText().toString().equals("ANDROID IS BETTER THAN IPHONE \nANDROID IS BETTER THAN IPHONE")) {
                    textView14.setText("AGRICULTURE \n");
                    touchCounter = 46;
                    writeTextTouch("\n" + "\n" + "ANDROID IS BETTER THAN IPHONE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("ANDROID IS BETTER THAN IPHONE \n");
                }
                break;
            case 46:
                if (textView14.getText().toString().equals("AGRICULTURE \nAGRICULTURE")) {
                    textView14.setText("I HAD EGGS FOR BREAKFAST TODAY \n");
                    touchCounter = 47;
                    writeTextTouch("\n" + "\n" + "AGRICULTURE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("AGRICULTURE \n");
                }
                break;
            case 47:
                if (textView14.getText().toString().equals("I HAD EGGS FOR BREAKFAST TODAY \nI HAD EGGS FOR BREAKFAST TODAY")) {
                    textView14.setText("THE BAR IS WHERE I GO \n");
                    touchCounter = 48;
                    writeTextTouch("\n" + "\n" + "I HAD EGGS FOR BREAKFAST TODAY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    touchBlock = 4;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I HAD EGGS FOR BREAKFAST TODAY \n");
                }
                break;
            case 48:
                if (textView14.getText().toString().equals("THE BAR IS WHERE I GO \nTHE BAR IS WHERE I GO")) {
                    textView14.setText("BLOCK 3 IS DONE !! \n");
                    touchCounter = 102;
                    writeTextTouch("\n" + "\n" + "THE BAR IS WHERE I GO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THE BAR IS WHERE I GO \n");
                }
                break;
            case 102:
                touchError = 0;
                touchCounter = 49;
                touchBlock = 4;
                elapsedTimeTouch = 0;
                break;
            default:
                textView14.setText("ENDD \n");
        }
    }

    public void touchBlock4(){
        switch (touchCounter){
            case 49:
                textView14.setText("I AM A CAIROKEE FAN \n");
                touchCounter = 50;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();
                break;
            case 50:
                if (textView14.getText().toString().equals("I AM A CAIROKEE FAN \nI AM A CAIROKEE FAN")) {
                    textView14.setText("MY DOG BITES \n");
                    touchCounter = 51;
                    writeTextTouch("\n" + "\n" + "I AM A CAIROKEE FAN : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I AM A CAIROKEE FAN \n");
                }
                break;
            case 51:
                if (textView14.getText().toString().equals("MY DOG BITES \nMY DOG BITES")) {
                    textView14.setText("BACHELOR \n");
                    touchCounter = 52;
                    writeTextTouch("\n" + "\n" + "MY DOG BITES : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY DOG BITES \n");
                }
                break;
            case 52:
                if (textView14.getText().toString().equals("BACHELOR \nBACHELOR")) {
                    textView14.setText("AMWAJ \n");
                    touchCounter = 53;
                    writeTextTouch("\n" + "\n" + "BACHELOR : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("BACHELOR \n");
                }
                break;
            case 53:
                if (textView14.getText().toString().equals("AMWAJ \nAMWAJ")) {
                    textView14.setText("WEAR A MASK FOR SAFETY \n");
                    touchCounter = 54;
                    writeTextTouch("\n" + "\n" + "AMWAJ : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("AMWAJ \n");
                }
                break;
            case 54:
                if (textView14.getText().toString().equals("WEAR A MASK FOR SAFETY \nWEAR A MASK FOR SAFETY")) {
                    textView14.setText("WASH YOUR HANDS \n");
                    touchCounter = 55;
                    writeTextTouch("\n" + "\n" + "WEAR A MASK FOR SAFETY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("WEAR A MASK FOR SAFETY \n");
                }
                break;
            case 55:
                if (textView14.getText().toString().equals("WASH YOUR HANDS \nWASH YOUR HANDS")) {
                    textView14.setText("MY FAVORITE COLOR IS BLUE \n");
                    touchCounter = 56;
                    writeTextTouch("\n" + "\n" + "WASH YOUR HANDS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("WASH YOUR HANDS \n");
                }
                break;
            case 56:
                if (textView14.getText().toString().equals("MY FAVORITE COLOR IS BLUE \nMY FAVORITE COLOR IS BLUE")) {
                    textView14.setText("I WANT TO GO TO THE BATHROOM \n");
                    touchCounter = 57;
                    writeTextTouch("\n" + "\n" + "MY FAVORITE COLOR IS BLUE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY FAVORITE COLOR IS BLUE \n");
                }
                break;
            case 57:
                if (textView14.getText().toString().equals("I WANT TO GO TO THE BATHROOM \nI WANT TO GO TO THE BATHROOM")) {
                    textView14.setText("THIS DOG IS SO CUTE \n");
                    touchCounter = 58;
                    writeTextTouch("\n" + "\n" + "I WANT TO GO TO THE BATHROOM : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I WANT TO GO TO THE BATHROOM \n");
                }
                break;
            case 58:
                if (textView14.getText().toString().equals("THIS DOG IS SO CUTE \nTHIS DOG IS SO CUTE")) {
                    textView14.setText("I WANT TO SEE MY FRIENDS \n");
                    touchCounter = 59;
                    writeTextTouch("\n" + "\n" + "THIS DOG IS SO CUTE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THIS DOG IS SO CUTE \n");
                }
                break;
            case 59:
                if (textView14.getText().toString().equals("I WANT TO SEE MY FRIENDS \nI WANT TO SEE MY FRIENDS")) {
                    textView14.setText("MESSI OR RONALDO \n");
                    touchCounter = 60;
                    writeTextTouch("\n" + "\n" + "I WANT TO SEE MY FRIENDS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I WANT TO SEE MY FRIENDS \n");
                }
                break;
            case 60:
                if (textView14.getText().toString().equals("MESSI OR RONALDO \nMESSI OR RONALDO")) {
                    textView14.setText("I AM FINE THANK YOU \n");
                    touchCounter = 61;
                    writeTextTouch("\n" + "\n" + "MESSI OR RONALDO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MESSI OR RONALDO \n");
                }
                break;
            case 61:
                if (textView14.getText().toString().equals("I AM FINE THANK YOU \nI AM FINE THANK YOU")) {
                    textView14.setText("LETS GO OUT TOMORROW \n");
                    touchCounter = 62;
                    writeTextTouch("\n" + "\n" + "I AM FINE THANK YOU : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I AM FINE THANK YOU \n");
                }
                break;
            case 62:
                if (textView14.getText().toString().equals("LETS GO OUT TOMORROW \nLETS GO OUT TOMORROW")) {
                    textView14.setText("I LIKE YOUR OUTFIT \n");
                    touchCounter = 63;
                    writeTextTouch("\n" + "\n" + "LETS GO OUT TOMORROW : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("LETS GO OUT TOMORROW \n");
                }
                break;
            case 63:
                if (textView14.getText().toString().equals("I LIKE YOUR OUTFIT \nI LIKE YOUR OUTFIT")) {
                    textView14.setText("I PREFER APPLE OVER SAMSUNG \n");
                    touchCounter = 64;
                    writeTextTouch("\n" + "\n" + "I LIKE YOUR OUTFIT : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LIKE YOUR OUTFIT \n");
                }
                break;
            case 64:
                if (textView14.getText().toString().equals("I PREFER APPLE OVER SAMSUNG \nI PREFER APPLE OVER SAMSUNG")) {
                    textView14.setText("BLOCK 4 IS DONE !! \n");
                    touchCounter = 103;
                    writeTextTouch("\n" + "\n" + "I PREFER APPLE OVER SAMSUNG : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I PREFER APPLE OVER SAMSUNG \n");
                }
                break;
            case 103:
                touchError = 0;
                touchCounter = 65;
                touchBlock = 5;
                elapsedTimeTouch = 0;
                break;
            default:
                textView14.setText("ENDD \n");
        }
    }

    public void touchBlock5(){
        switch (touchCounter){
            case 65:
                textView14.setText("I PREFER NIKE OVER ADIDAS");
                touchCounter = 66;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();

                break;
            case 66:
                if (textView14.getText().toString().equals("I PREFER NIKE OVER ADIDAS \nI PREFER NIKE OVER ADIDAS")) {
                    textView14.setText("I LOVE PLAYING TENNIS \n");
                    touchCounter = 67;
                    writeTextTouch("\n" + "\n" + "I PREFER NIKE OVER ADIDAS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I PREFER NIKE OVER ADIDAS \n");
                }
                break;
            case 67:
                if (textView14.getText().toString().equals("I LOVE PLAYING TENNIS \nI LOVE PLAYING TENNIS")) {
                    textView14.setText("MY DREAM IS TO FLY \n");
                    touchCounter = 68;
                    writeTextTouch("\n" + "\n" + "I LOVE PLAYING TENNIS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE PLAYING TENNIS \n");
                }
                break;
            case 68:
                if (textView14.getText().toString().equals("MY DREAM IS TO FLY \nMY DREAM IS TO FLY")) {
                    textView14.setText("YOU HAVE TO SAY SORRY \n");
                    touchCounter = 69;
                    writeTextTouch("\n" + "\n" + "MY DREAM IS TO FLY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY DREAM IS TO FLY \n");
                }
                break;
            case 69:
                if (textView14.getText().toString().equals("YOU HAVE TO SAY SORRY \nYOU HAVE TO SAY SORRY")) {
                    textView14.setText("I HATE YOU I LOVE YOU \n");
                    touchCounter = 70;
                    writeTextTouch("\n" + "\n" + "YOU HAVE TO SAY SORRY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("YOU HAVE TO SAY SORRY \n");
                }
                break;
            case 70:
                if (textView14.getText().toString().equals("I HATE YOU I LOVE YOU \nI HATE YOU I LOVE YOU")) {
                    textView14.setText("I MISS YOU SO MUCH \n");
                    touchCounter = 71;
                    writeTextTouch("\n" + "\n" + "I HATE YOU I LOVE YOU : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I HATE YOU I LOVE YOU \n");
                }
                break;
            case 71:
                if (textView14.getText().toString().equals("I MISS YOU SO MUCH \nI MISS YOU SO MUCH")) {
                    textView14.setText("I NEED TO FIND MY BAG \n");
                    touchCounter = 72;
                    writeTextTouch("\n" + "\n" + "I MISS YOU SO MUCH : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I MISS YOU SO MUCH \n");
                }
                break;
            case 72:
                if (textView14.getText().toString().equals("I NEED TO FIND MY BAG \nI NEED TO FIND MY BAG")) {
                    textView14.setText("MY ROOM NEEDS TO BE REARRANGED \n");
                    touchCounter = 73;
                    writeTextTouch("\n" + "\n" + "I NEED TO FIND MY BAG : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I NEED TO FIND MY BAG \n");
                }
                break;
            case 73:
                if (textView14.getText().toString().equals("MY ROOM NEEDS TO BE REARRANGED \nMY ROOM NEEDS TO BE REARRANGED")) {
                    textView14.setText("I AM AFRAID OF CORONAVIRUS \n");
                    touchCounter = 74;
                    writeTextTouch("\n" + "\n" + "MY ROOM NEEDS TO BE REARRANGED : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY ROOM NEEDS TO BE REARRANGED \n");
                }
                break;
            case 74:
                if (textView14.getText().toString().equals("I AM AFRAID OF CORONAVIRUS \nI AM AFRAID OF CORONAVIRUS")) {
                    textView14.setText("MY LAPTOP IS RUNNING OUT OF BATTERY \n");
                    touchCounter = 75;
                    writeTextTouch("\n" + "\n" + "I AM AFRAID OF CORONAVIRUS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I AM AFRAID OF CORONAVIRUS \n");
                }
                break;
            case 75:
                if (textView14.getText().toString().equals("MY LAPTOP IS RUNNING OUT OF BATTERY \nMY LAPTOP IS RUNNING OUT OF BATTERY")) {
                    textView14.setText("NO WOMAN NO CRY \n");
                    touchCounter = 76;
                    writeTextTouch("\n" + "\n" + "MY LAPTOP IS RUNNING OUT OF BATTERY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("MY LAPTOP IS RUNNING OUT OF BATTERY \n");
                }
                break;
            case 76:
                if (textView14.getText().toString().equals("NO WOMAN NO CRY \nNO WOMAN NO CRY")) {
                    textView14.setText("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \n");
                    touchCounter = 77;
                    writeTextTouch("\n" + "\n" + "NO WOMAN NO CRY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("NO WOMAN NO CRY \n");
                }
                break;
            case 77:
                if (textView14.getText().toString().equals("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \nNOW AND THEN I THINK OF WHEN WE WERE TOGETHER")) {
                    textView14.setText("I FEEL VERY HAPPY \n");
                    touchCounter = 78;
                    writeTextTouch("\n" + "\n" + "NOW AND THEN I THINK OF WHEN WE WERE TOGETHER : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("NOW AND THEN I THINK OF WHEN WE WERE TOGETHER \n");
                }
                break;
            case 78:
                if (textView14.getText().toString().equals("I FEEL VERY HAPPY \nI FEEL VERY HAPPY")) {
                    textView14.setText("I SET FIRE TO THE RAIN \n");
                    touchCounter = 79;
                    writeTextTouch("\n" + "\n" + "I FEEL VERY HAPPY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I FEEL VERY HAPPY \n");
                }
                break;
            case 79:
                if (textView14.getText().toString().equals("I SET FIRE TO THE RAIN \nI SET FIRE TO THE RAIN")) {
                    textView14.setText("NOTHING IS KEEPING ME BACK \n");
                    touchCounter = 80;
                    writeTextTouch("\n" + "\n" + "I SET FIRE TO THE RAIN : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I SET FIRE TO THE RAIN \n");
                }
                break;
            case 80:
                if (textView14.getText().toString().equals("NOTHING IS KEEPING ME BACK \nNOTHING IS KEEPING ME BACK")) {
                    textView14.setText("BLOCK 5 IS DONE !! \n");
                    touchCounter = 104;
                    writeTextTouch("\n" + "\n" + "NOTHING IS KEEPING ME BACK : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("NOTHING IS KEEPING ME BACK \n");
                }
                break;
            case 104:
                touchError = 0;
                touchCounter = 81;
                touchBlock = 6;
                elapsedTimeTouch = 0;
                break;
            default:
                textView14.setText("ENDD \n");
        }
    }

    public void touchBlock6(){
        switch (touchCounter){
            case 81:
                textView14.setText("SWEET DREAMS ARE MADE OF THIS");
                touchCounter = 82;
                elapsedTimeTouch = 0;
                touchError = 0;
                startTimeTouch = System.nanoTime();
                break;
            case 82:
                if (textView14.getText().toString().equals("SWEET DREAMS ARE MADE OF THIS \nSWEET DREAMS ARE MADE OF THIS")) {
                    textView14.setText("IT IS TOO LATE TO SAY SORRY \n");
                    touchCounter = 83;
                    writeTextTouch("\n" + "\n" + "SWEET DREAMS ARE MADE OF THIS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("SWEET DREAMS ARE MADE OF THIS \n");
                }
                break;
            case 83:
                if (textView14.getText().toString().equals("IT IS TOO LATE TO SAY SORRY \nIT IS TOO LATE TO SAY SORRY")) {
                    textView14.setText("THE WEATHER IS TOO COLD \n");
                    touchCounter = 84;
                    writeTextTouch("\n" + "\n" + "IT IS TOO LATE TO SAY SORRY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("IT IS TOO LATE TO SAY SORRY \n");
                }
                break;
            case 84:
                if (textView14.getText().toString().equals("THE WEATHER IS TOO COLD \nTHE WEATHER IS TOO COLD")) {
                    textView14.setText("BAD BOYS \n");
                    touchCounter = 85;
                    writeTextTouch("\n" + "\n" + "THE WEATHER IS TOO COLD : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THE WEATHER IS TOO COLD \n");
                }
                break;
            case 85:
                if (textView14.getText().toString().equals("BAD BOYS \nBAD BOYS")) {
                    textView14.setText("KENTUCKY FRIED CHICKEN \n");
                    touchCounter = 86;
                    writeTextTouch("\n" + "\n" + "BAD BOYS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("BAD BOYS \n");
                }
                break;
            case 86:
                if (textView14.getText().toString().equals("KENTUCKY FRIED CHICKEN \nKENTUCKY FRIED CHICKEN")) {
                    textView14.setText("THE GERMAN UNIVERSITY IN CAIRO \n");
                    touchCounter = 87;
                    writeTextTouch("\n" + "\n" + "KENTUCKY FRIED CHICKEN : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("KENTUCKY FRIED CHICKEN \n");
                }
                break;
            case 87:
                if (textView14.getText().toString().equals("THE GERMAN UNIVERSITY IN CAIRO \nTHE GERMAN UNIVERSITY IN CAIRO")) {
                    textView14.setText("BLACK LIVES MATTER \n");
                    touchCounter = 88;
                    writeTextTouch("\n" + "\n" + "THE GERMAN UNIVERSITY IN CAIRO : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THE GERMAN UNIVERSITY IN CAIRO \n");
                }
                break;
            case 88:
                if (textView14.getText().toString().equals("BLACK LIVES MATTER \nBLACK LIVES MATTER")) {
                    textView14.setText("FAST AND FURIOUS \n");
                    touchCounter = 89;
                    writeTextTouch("\n" + "\n" + "BLACK LIVES MATTER : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("BLACK LIVES MATTER \n");
                }
                break;
            case 89:
                if (textView14.getText().toString().equals("FAST AND FURIOUS \nFAST AND FURIOUS")) {
                    textView14.setText("HARRY POTTER \n");
                    touchCounter = 90;
                    writeTextTouch("\n" + "\n" + "FAST AND FURIOUS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("FAST AND FURIOUS \n");
                }
                break;
            case 90:
                if (textView14.getText().toString().equals("HARRY POTTER \nHARRY POTTER")) {
                    textView14.setText("BLUE HOLE \n");
                    touchCounter = 91;
                    writeTextTouch("\n" + "\n" + "HARRY POTTER : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("HARRY POTTER \n");
                }
                break;
            case 91:
                if (textView14.getText().toString().equals("BLUE HOLE \nBLUE HOLE")) {
                    textView14.setText("THESE SONGS MAKE A GOOD VIBE \n");
                    touchCounter = 92;
                    writeTextTouch("\n" + "\n" + "BLUE HOLE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("BLUE HOLE \n");
                }
                break;
            case 92:
                if (textView14.getText().toString().equals("THESE SONGS MAKE A GOOD VIBE \nTHESE SONGS MAKE A GOOD VIBE")) {
                    textView14.setText("RAP GOD \n");
                    touchCounter = 93;
                    writeTextTouch("\n" + "\n" + "THESE SONGS MAKE A GOOD VIBE : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("THESE SONGS MAKE A GOOD VIBE \n");
                }
                break;
            case 93:
                if (textView14.getText().toString().equals("RAP GOD \nRAP GOD")) {
                    textView14.setText("I LOVE WATHCING STARS \n");
                    touchCounter = 94;
                    writeTextTouch("\n" + "\n" + "RAP GOD : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("RAP GOD \n");
                }
                break;
            case 94:
                if (textView14.getText().toString().equals("I LOVE WATHCING STARS \nI LOVE WATHCING STARS")) {
                    textView14.setText("DARKNESS IS SCARY \n");
                    touchCounter = 95;
                    writeTextTouch("\n" + "\n" + "I LOVE WATHCING STARS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("I LOVE WATHCING STARS \n");
                }
                break;
            case 95:
                if (textView14.getText().toString().equals("DARKNESS IS SCARY \nDARKNESS IS SCARY")) {
                    textView14.setText("ALL MY FRIENDS ARE HEATHENS \n");
                    touchCounter = 96;
                    writeTextTouch("\n" + "\n" + "DARKNESS IS SCARY : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("DARKNESS IS SCARY \n");
                }
                break;
            case 96:
                if (textView14.getText().toString().equals("ALL MY FRIENDS ARE HEATHENS \nALL MY FRIENDS ARE HEATHENS")) {
                    textView14.setText("BLOCK 6 IS DONE !! \n");
                    touchCounter = 105;
                    writeTextTouch("\n" + "\n" + "ALL MY FRIENDS ARE HEATHENS : " + "\n" + "Touch Elapsed Time : " + elapsedTimeTouch + " s" + "\n" + "Touch Errors : " + touchError);
                    elapsedTimeTouch = 0;
                    touchError = 0;
                    startTimeTouch = System.nanoTime();
                } else {
                    textView14.setText("ALL MY FRIENDS ARE HEATHENS \n");
                }
                break;

            case 105:
                elapsedTimeTouch = 0;
                touchError = 0;
            default:
                textView14.setText("ENDD \n");
        }
    }

}
