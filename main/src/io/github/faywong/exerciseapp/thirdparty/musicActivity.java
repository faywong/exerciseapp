package io.github.faywong.exerciseapp.thirdparty;

import io.github.faywong.exerciseapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List; 
import android.app.ListActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class musicActivity extends ListActivity {
	//���Ŷ���
	private MediaPlayer myMediaPlayer;
	//�����б�
	private List<String> myMusicList=new ArrayList<String>();
	//��ǰ���Ÿ��������
	private int currentListItem=0;
	//���ֵ�·��
	private static final String MUSIC_PATH=new String("/sdcard/");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.music_main);
        myMediaPlayer=new MediaPlayer();
        
        findView();
        musicList();
        listener();
    }
    
    
    //������
    void musicList(){
    	File home=new File(MUSIC_PATH);
    	if(home.listFiles(new MusicFilter()).length>0){
    		for(File file:home.listFiles(new MusicFilter())){
    			myMusicList.add(file.getName());
    		}
    		ArrayAdapter<String> musicList=new ArrayAdapter<String>
    		(musicActivity.this,R.layout.musicitem, myMusicList);
    		setListAdapter(musicList);
    	}
    }
    
    //��ȡ��ť
   void findView(){
	   mViewHolder.start=(Button)findViewById(R.id.start);
	   mViewHolder.stop=(Button)findViewById(R.id.stop);
	   mViewHolder.next=(Button)findViewById(R.id.next);
	   mViewHolder.pause=(Button)findViewById(R.id.pause);
	   mViewHolder.last=(Button)findViewById(R.id.last);
   }
   
   
   //�����¼�
   void listener(){
	   //ֹͣ
	   mViewHolder.stop.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(myMediaPlayer.isPlaying()){
				myMediaPlayer.reset();
			}
		}
	});
	   //��ʼ
	   mViewHolder.start.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
		}
	});
	   //��һ��
	   mViewHolder.next.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nextMusic();
		}
	});
	   //��ͣ
	   mViewHolder.pause.setOnClickListener(new OnClickListener() {
		
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
	   //��һ��
	   mViewHolder.last.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			lastMusic();
		}
	});
	   
   }
   
   //�������� 
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
   
   //��һ��
   void nextMusic(){
	   if(++currentListItem>=myMusicList.size()){
		   currentListItem=0;
	   }
	   else{
		   playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
	   }
   }
   
   //��һ��
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
   
   //���û�����ʱ�������ֲ��ͷ����ֶ���
	   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		   if(keyCode==KeyEvent.KEYCODE_BACK){
			   myMediaPlayer.stop();
			   myMediaPlayer.release();
			   this.finish();
			   return true;
		   }
		return super.onKeyDown(keyCode, event);
	}
   
	   //��ѡ���б���ʱ�������� 
	   @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		currentListItem=position;
		playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
	}
   
}