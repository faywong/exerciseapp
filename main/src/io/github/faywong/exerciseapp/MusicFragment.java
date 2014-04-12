package io.github.faywong.exerciseapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.faywong.exerciseapp.SurfFragment.MyWebViewClient;
import io.github.faywong.exerciseapp.thirdparty.MusicFilter;
import io.github.faywong.exerciseapp.thirdparty.musicActivity;
import io.github.faywong.exerciseapp.thirdparty.viewHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MusicFragment extends ListFragment{
	
	String[] presidents = {     
	        "Dwight D. Eisenhower",     
	        "John F. Kennedy",     
	        "Lyndon B. Johnson",     
	        "Richard Nixon",     
	        "Gerald Ford",     
	        "Jimmy Carter",     
	        "Ronald Reagan",     
	        "George H. W. Bush",     
	        "Bill Clinton",     
	        "George W. Bush",     
	        "Barack Obama"
	    };    
	//播放对象
		private MediaPlayer myMediaPlayer;
		//播放列表
		private List<String> myMusicList=new ArrayList<String>();
		//当前播放歌曲的索引
		private int currentListItem=0;
		//音乐的路径
		private static final String MUSIC_PATH=new String("/sdcard/");
		
	private static String TAG = "MusicFragment";

    

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	public void windowFocusChanged(boolean hasFocus) {

	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View parentLayout = inflater.inflate(R.layout.music_main, container, false);
		
		myMediaPlayer=new MediaPlayer();
	        
        findView(parentLayout);
        musicList();
       listener();

		return parentLayout;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {     
        super.onCreate(savedInstanceState);         
    }  
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	  //绑定音乐
    void musicList(){
    	File home=new File(MUSIC_PATH);
    	if(home.listFiles(new MusicFilter()).length>0){
    		for(File file:home.listFiles(new MusicFilter())){
    			myMusicList.add(file.getName());
    		}
    		ArrayAdapter<String> musicList=new ArrayAdapter<String>(getActivity(),R.layout.musicitme, myMusicList);
    		setListAdapter(musicList);
    	}
    }
    
    //获取按钮
   void findView(View parentView){
	   viewHolder.start=(Button)parentView.findViewById(R.id.start);
	   viewHolder.stop=(Button)parentView.findViewById(R.id.stop);
	   viewHolder.next=(Button)parentView.findViewById(R.id.next);
	   viewHolder.pause=(Button)parentView.findViewById(R.id.pause);
	   viewHolder.last=(Button)parentView.findViewById(R.id.last);
   }
   
   
   //监听事件
   void listener(){
	   //停止
	   viewHolder.stop.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(myMediaPlayer.isPlaying()){
				myMediaPlayer.reset();
			}
		}
	});
	   //开始
	   viewHolder.start.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
		}
	});
	   //下一首
	   viewHolder.next.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nextMusic();
		}
	});
	   //暂停
	   viewHolder.pause.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(myMediaPlayer.isPlaying()){
				myMediaPlayer.pause();
			}else{
				myMediaPlayer.start();
			}
		}
	});
	   //上一首
	   viewHolder.last.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			lastMusic();
		}
	});
	   
   }
   
   //播放音乐 
   void playMusic(String path){
	   try { 
		myMediaPlayer.reset();
		myMediaPlayer.setDataSource(path);
		myMediaPlayer.prepare();
		myMediaPlayer.start();
		myMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				nextMusic();
			}
		});
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
   }
   
   //下一首
   void nextMusic(){
	   if(++currentListItem>=myMusicList.size()){
		   currentListItem=0;
	   }
	   else{
		   playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
	   }
   }
   
   //上一首
   void lastMusic(){
	   if(currentListItem!=0)
		   {
	   if(--currentListItem>=0){
		   currentListItem=myMusicList.size();
	   } else{
		   playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
	   }
		  }  else{
		   playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
	   }
   }
   
   @Override
public void onListItemClick(ListView l, View v, int position, long id) {
  		// TODO Auto-generated method stub
  		currentListItem=position;
  		playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
  	}
	
}
