package com.max.cellularfragment.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.max.cellularfragment.R;

import java.util.ArrayList;
import java.util.Arrays;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return new Surface(getContext());
        //return new Surface(getActivity().getApplicationContext());
        //return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}



class Surface extends SurfaceView implements SurfaceHolder.Callback {
    private float x = -1000;
    private float y = -1000;
    private int r;
    final String TAG = "TEST_SPACE";

    public Surface(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        r = 0;
        Log.d(TAG, x + " " + y);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new DrawThread(this.getContext(), surfaceHolder).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class DrawThread extends Thread{
        private SurfaceHolder surfaceHolder;
        private volatile boolean running = true;
        Paint paint = new Paint();

        public DrawThread(Context context, SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }
        public void requestStop(){
            running=false;
        }

        @Override
        public void run() {
            while (running){
                Canvas canvas = surfaceHolder.lockCanvas();
                Log.d("MAESTRO", "!");
                if (canvas != null) {

                    canvas.drawColor(Color.BLACK);
                    paint.setColor(Color.BLACK);

                    int n=canvas.getWidth();
                    //int n=121;
                    Integer state[] = new Integer[n];
                    Arrays.fill(state, 0);
                    state[n/2]=1;
                    ArrayList<Integer[]> states = new ArrayList<Integer[]>();
                    states.add(state);

                    int rule = 26; //18, 22, 26, 13
                    for (int it=0; it<canvas.getHeight(); it++){
                        Integer ls[] = new Integer[n];
                        Arrays.fill(ls, 0);
                        for (int i=0; i<n; i++){
                            int index;
                            if (i-1<0){
                                index = states.get(states.size()-1)[i]*2+states.get(states.size()-1)[n-1]*4+
                                        states.get(states.size()-1)[(i+1)%n];
                            }
                            else{
                                index = states.get(states.size()-1)[i]*2+states.get(states.size()-1)[i-1]*4+
                                        states.get(states.size()-1)[(i+1)%n];
                            }
                            //for (int j=0; j<n; j++){
                            ls[i]=(rule&((int)(Math.pow(2, index))))>0?1:0;

                            //}
                        }
                        states.add(ls);
                    }

                    long flag=0;
                    for (int i=0; i<states.size(); i++){
                        for (int j=0; j<n; j++){

                            if (states.get(i)[j]==1){
                                flag++;
                                if (flag%4==0) paint.setColor(Color.WHITE);
                                else if (flag%4==1) paint.setColor(Color.YELLOW);
                                else paint.setColor(Color.argb(255, 200, 100, 20));
                                canvas.drawPoint( j,i, paint);
                            }

                            //out.print(states.get(i)[j]+" ");
                        }

                        //out.println();
                    }




                    getHolder().unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



