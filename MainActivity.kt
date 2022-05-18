package com.example.contentresolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contentresolver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var storagePermission: ActivityResultLauncher<String>
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("GRANT", "BEFORE")
        /*storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){//왜인지 모르겠지만 자꾸 no adapter attached skipping layout 에러 뜸.
            Log.d("GRANT", "ENTER")
            if(it){
                Log.d("GRANT", "SUCCESS")
                startProcess()
            }else{
                Log.d("GRANT", "fail")
                Toast.makeText(baseContext,
                "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                Toast.LENGTH_LONG).show()
                finish()
            }
        }*/
        startProcess()
        Log.d("GRANT", "AFTER")
    }
    fun startProcess(){
        val adapter = MusicRecyclerAdapter()//어댑터 생성
        adapter.musicList.addAll(getMusicList())//음원 데이터 넘기기
        binding.recyclerView.adapter = adapter//데이터가 담긴 어댑터 리사이클러뷰에 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(this)//레이아웃 매니지 설정
    }
    fun getMusicList(): List<Music>{//음원 읽어오기
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI//음원 정보 주소 저장
        val proj = arrayOf(//음원 정보 테이블에서 읽어올 컬럼명을 배열로 정의
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        val cursor = contentResolver.query(listUrl, proj, null, null, null)//쿼리 실행결과를 커서로 반환
        val musicList = mutableListOf<Music>()//전달받은 데이터를 꺼내 저장할 목록 변수

        while(cursor?.moveToNext() == true){//커서를 이동하면서 한줄씩 읽기
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)

            val music = Music(id, title, artist, albumId, duration)
            musicList.add(music)
        }
        return musicList //반환
    }
}