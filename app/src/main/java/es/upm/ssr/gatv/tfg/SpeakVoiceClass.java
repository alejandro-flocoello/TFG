package es.upm.ssr.gatv.tfg;


import java.util.Locale;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Button;


public class SpeakVoiceClass  implements OnInitListener{

    public  TextToSpeech tts ;
    private Button btnSpeak;
    private String messageText;

    public SpeakVoiceClass(TextToSpeech tts){
        tts = this.tts;
    }

    public void onInit(int status) {

        if ( status == TextToSpeech.SUCCESS ) {

            //establece idioma por defecto, en este caso el idioma es español
            int result = tts.setLanguage( Locale.getDefault() );

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                btnSpeak.setEnabled(false);

                Log.e("TTS", "El idioma no está soportado");
            } else {
                btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Inicialización fallida!");
        }
    }

    public void sintetiza(String texto) {
        tts.speak( texto, TextToSpeech.QUEUE_FLUSH, null );
        Log.e("TTS", "Sintetización de voz en curso ...");
    }

    //Cuando se cierra la aplicacion se destruye el TTS

    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


}