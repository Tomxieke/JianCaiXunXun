package com.ciist.xunxun.app.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.game.Config;
import com.ciist.xunxun.app.game.PuzzGameActivity;

public class PuzzGameView extends View implements DialogInterface.OnClickListener {
	private int w,h;	//游戏区域宽高
	private Bitmap img;		//整图的图片
	private Paint paint;	//至少有一个画笔
	private Bitmap[] bitmap;	//被分割的图片的数组
	private Rect[] rect;	//对应于图片的矩形块
	private int point;		//空块的位置
	private int array[];	//块数组
	private int array2[][];	//块数组
	private int level;	//这里为图片分为几*几的大小
	private int emptyBlock;	//空块的值
	private int clickBlock;	//手触摸屏幕时点击的块

	public boolean isTouch = false;
	
	public PuzzGameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		img = BitmapFactory.decodeResource(getResources(), R.mipmap.ciist_xunxun_game_testimg);
//		switch(Config.imageId){
//		case R.id.iv1:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon1);
//			break;
//		case R.id.iv2:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon2);
//			break;
//		case R.id.iv3:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon3);
//			break;
//		case R.id.iv4:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon4);
//			break;
//		case R.id.iv5:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon5);
//			break;
//		case R.id.iv6:
//			img = BitmapFactory.decodeResource(getResources(), R.drawable.icon6);
//			break;
//		}
		
		paint = new Paint();	
		Config.bushu = 0;	//将步数初始化为0
		level = Config.nandu;	//难度,也就是图形被分成了几个格子
		
		rect = new Rect[level*level];	
		bitmap = new Bitmap[level*level];
		array = new int[level*level];
		array2 = new int[level][level];
		
		point = 0;

		//初始化游戏区域宽高


//		this.w = Config.metrics.widthPixels-30*2;
//		this.h = Config.metrics.heightPixels/2;

		this.w = Config.metrics.widthPixels - convertPxOrDip(getContext(),26);
		this.h = Config.metrics.widthPixels;

		//初始化图片
		paint.setColor(Color.WHITE);
		
		img = Bitmap.createScaledBitmap(img, w, h, true);
		
		for( int y = 0; y < level; y++){
			for(int x = 0; x < level; x++){
				//分割图片
				bitmap[y*level+x] = Bitmap.createBitmap(img,x * (w / level) + 2, y * (h / level) + 2,w / level - 2,h / level - 2);
				rect[y*level+x] = new Rect(x * (w / level) + 2, y * (h / level) + 2, (x+1) * w / level - 2, (y+1) * h / level - 2 );
				array[y*level+x] = y*level+x;
				array2[y][x] = y*level+x;
				//图片将要显示的位置
			}
		}
		emptyBlock = level*level-1;
		point = level*level-1;
		//循环打乱
		for(int i = 0; i < level*level*10;i++){
			yyy();
		}

	}


	//转换px为dip
	private int convertPxOrDip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}


	//使用自定义的方法打乱成怎么都能复原
	private void yyy(){
		int ran = 0;
		
		List<Integer> tt = new ArrayList<Integer>(); 
		if(isLeft(point)){
			tt.add(0);
			ran++;
		}
		if(isRight(point)){
			tt.add(1);
			ran++;
		}
		if(isTop(point)){
			tt.add(2);
			ran++;
		}
		if(isButtom(point)){
			tt.add(3);
			ran++;
		}
		int tem;
		switch(tt.get((int) (Math.random()* ran))){
		case 0:	//左右上下
			tem = array[point];
			array[point] = array[point-1];
			array[point-1] = tem;
			point--;
			break;
		case 1:
			tem = array[point];
			array[point] = array[point+1];
			array[point+1] = tem;
			point++;
			break;
		case 2:
			tem = array[point];
			array[point] = array[point-level];
			array[point-level] = tem;
			point -= level;
			break;
		case 3:
			tem = array[point];
			array[point] = array[point+level];
			array[point+level] = tem;
			point += level;
			break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for( int y = 0; y < level; y++){
			for(int x = 0; x < level; x++){
				int u = array[y*level+x];
				if(level * level - 1 == u){	//如果是空块,画个灰色的
					paint.setColor(Color.GRAY);
					canvas.drawRect(rect[y*level+x], paint);
				}else{
					canvas.drawBitmap(bitmap[u], (float)rect[y*level+x].left, (float)rect[y*level+x].top, paint);
				}
			}
		}
		invalidate();
		super.onDraw(canvas);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isTouch){
			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					clickBlock = getTouchXY(event);
					break;
				case MotionEvent.ACTION_UP:
					int k = getTouchXY(event);	//被点击的块
					if(k != clickBlock){//如果手指点下的块与抬起时的块不同,则不算作某块被点击了
						return false;
					}

					if(k != -1){
						if(isLeft(k) ){	//空块在左边
							if( array[k-1] == level * level - 1){
								array[k-1] = array[k];
								array[k] = level * level - 1;
								Config.bushu++;
								emptyBlock = k;
								isOk(event);
								return true;
							}
						}
						if(isRight(k) ){
							if(array[k+1] == level * level - 1){
								array[k+1] = array[k];
								array[k] = level * level - 1;
								Config.bushu++;
								emptyBlock = k;
								isOk(event);
								return true;
							}
						}
						if(isTop(k)){
							if(array[k-level] == level * level - 1){
								array[k-level] = array[k];
								array[k] = level * level - 1;
								Config.bushu++;
								emptyBlock = k;
								isOk(event);
								return true;
							}
						}
						if(isButtom(k)){
							if(array[k+level] == level * level - 1){
								array[k+level] = array[k];
								array[k] = level * level - 1;
								Config.bushu++;
								emptyBlock = k;
								isOk(event);
								return true;
							}
						}
					}
					break;
			}
		}
		return true;
	}
	
	private int getTouchXY(MotionEvent event){	//这个方法是取出哪个块被点击了
		for( int y = 0; y < level; y++){
			for(int x = 0; x < level; x++){
				if(rect[y*level+x].contains((int)event.getX(), (int)event.getY())){
					//Log.e("dianji",array[y*level+x] + "");
					return y*level+x;
				}
			}
		}
		return -1;
	}

	/*private int find(int n){
		for(int i = 0; i < o*o ;i++){
			if(array[i] == n){
				return i;
			}
		}
		return -1;
	}*/
	
	private boolean isLeft(int k){	//判断被点的块的左边是否超出边缘
		for(int i = 0; i < level ;i++){
			if( k == level * i){
				return false;
			}
		}
		return true;	
	}
	private boolean isRight(int k){	
		for(int i = 0; i < level ;i++){
			if( k == level * (i+1)-1){
				return false;
			}
		}
		return true;	
	}
	private boolean isTop(int k){	
		for(int i = 0; i < level ;i++){
			if( k == i){
				return false;
			}
		}
		return true;	
	}
	private boolean isButtom(int k){	
		for(int i = 0; i < level ;i++){
			if( k == level * level - i-1){
				return false;
			}
		}
		return true;	
	}


	private boolean isOk(MotionEvent event){

		int n = 0;
		int p = 0;

		for( int i = 0; i < array.length-1; i++){

			//判断块的位置是否归位
			for( int y = 0; y < level; y++) {
				for (int x = 0; x < level; x++) {
					if (array2[y][x] == n){
						p = p + 1;
						//Log.e("ppp","p = " + p);
						mOnPuzzOverListener.setProgress(p);
					}
				}
			}

			n = n+1;

			if(array[i+1]-array[i] != 1 ){
				return false;
			}	
		}

		mOnPuzzOverListener.stopUi();

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("完成");
		builder.setMessage("用时:" + (int) ((System.currentTimeMillis() - Config.startTime) / 1000)
				+ "\n"
				+ "本次游戏体验结束！"
				+ "\n"
				+ "您的排名已上传到迅讯进行统计，"
				+ "\n"
				+ "排名最高者将会得到丰厚奖励哦！");

		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mOnPuzzOverListener.puzzOver();
			}
		});

		builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getContext(), "分享到微信。。。", Toast.LENGTH_LONG).show();
			}
		});

		builder.create().show();

		return true;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {		
	}



	//回调
	public interface OnPuzzOverListener{
		void puzzOver();
		void stopUi();
		void setProgress(int p);
	}

	private OnPuzzOverListener mOnPuzzOverListener;

	public void setOnPuzzOverListener(OnPuzzOverListener onPuzzOverListener){
		mOnPuzzOverListener = onPuzzOverListener;
	}

}
