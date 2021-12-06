package genius;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;

public class Sons {
	AudioClip audioVermelho;
	AudioClip audioAzul;
	AudioClip audioAmarelo;
	AudioClip audioVerde;

	public Sons() { 
		URL somVermelho = getClass().getResource("som1.wav");  
		audioVermelho = Applet.newAudioClip(somVermelho); 

		URL somAzul = getClass().getResource("som2.wav");
		audioAzul = Applet.newAudioClip(somAzul); 

		URL somAmarelo = getClass().getResource("som3.wav");  
		audioAmarelo = Applet.newAudioClip(somAmarelo); 

		URL somVerde = getClass().getResource("som4.wav");  
		audioVerde = Applet.newAudioClip(somVerde);
	}   

	public AudioClip getAudioVermelho() {
		return audioVermelho;
	}
	public AudioClip getAudioAzul() {
		return audioAzul;
	}
	public AudioClip getAudioAmarelo() {
		return audioAmarelo;
	}
	public AudioClip getAudioVerde() {
		return audioVerde;
	}
}