package com.example.contentresolver

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contentresolver.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class MusicRecyclerAdapter: RecyclerView.Adapter<MusicRecyclerAdapter.Holder>() {
    var musicList = mutableListOf<Music>()//음악 목록 저장 변수
    var mediaPlayer: MediaPlayer? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {//아이템 레이아웃 바인딩 생성
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {//아이템 레이아웃에 데이터 출력
        val music = musicList.get(position)
        holder.setMusic(music)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        var musicUri: Uri? = null
        init{
            itemView.setOnClickListener {
                if(mediaPlayer != null){//설정된 값이 있으면
                    mediaPlayer?.release()//해제
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(itemView.context, musicUri)
                mediaPlayer?.start()
            }
            binding.buttonStop.setOnClickListener {//미니퀴즈 11-2)3
                if(mediaPlayer != null){//설정된 값이 있으면
                    mediaPlayer?.release()//해제
                    mediaPlayer = null
                }
            }

        }
        fun setMusic(music: Music) {
            binding.run{//run 함수를 이용하면 매번 binding.을 입력하지 않아도 됨.
                imageAlbum.setImageURI(music.getAlbumUri())//음악 앨범
                textArtist.text = music.artist//아티스트
                textTitle.text = music.title//제목

                val duration = SimpleDateFormat("mm:SS").format(music.duration)//길이 가져오기
                textDuration.text = duration
            }
            this.musicUri = music.getMusicUri()
        }

    }
}
