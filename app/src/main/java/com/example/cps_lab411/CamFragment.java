package com.example.cps_lab411;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;


public class CamFragment extends Fragment {

    // Camera IP
    private static final String url = "http://192.168.1.10:8080/stream?topic=/main_camera/image_raw";
    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View camView = inflater.inflate(R.layout.fragment_cam, container, false);

        libVlc = new LibVLC(camView.getContext());
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = camView.findViewById(R.id.videoLayout);

        // Fragment locked in landscape screen orientation

        return camView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mediaPlayer.attachViews(videoLayout, null, false, false);

        Media media = new Media(libVlc, Uri.parse(url));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=600");

        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        // Stop Live Stream
        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // Delete Live Stream
        mediaPlayer.release();
        libVlc.release();
    }

}
