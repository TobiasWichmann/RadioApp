package de.twproject.iubhradio;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.app.AlertDialog;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.app.ProgressDialog;

public class MainActivity extends AppCompatActivity {

    TextView titelElement;
    TextView interpretElement;
    TextView albumElement;
    TextView jahrElement;
    TextView playlistElement;
    TextView moderatorElement;
    Button playButton;
    Button stopButton;
    Button wunschButton;
    Button playlistVoteUpButton;
    Button playlistVoteDownButton;
    Button moderatorVoteUpButton;
    Button moderatorVoteDownButton;
    AlertDialog.Builder builder;
    ProgressDialog soundFileLadestatus;
    MediaPlayer soundFile;
    String aktuelleModeratorNummer = "smsto:016212345678"; // dieser Wert bezieht sich auf die aktuelle Radio-Hotline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bewertungsmethode für positive Bewertung der Playlist (Daumen hoch)
        playlistVoteUpButton = (Button) findViewById(R.id.buttonPlaylistVoteUp);
        builder = new AlertDialog.Builder(this);
        playlistVoteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Danke für dein Feedback", Toast.LENGTH_SHORT).show();
                playlistVoteUpButton.setVisibility(View.GONE);
                playlistVoteDownButton.setVisibility(View.GONE);
            }
        });

        //Bewertungsmethode für negative Bewertung der Playlist (Daumen runter)
        playlistVoteDownButton = (Button) findViewById(R.id.buttonPlaylistVoteDown);
        builder = new AlertDialog.Builder(this);
        playlistVoteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Danke für dein Feedback", Toast.LENGTH_SHORT).show();
                playlistVoteUpButton.setVisibility(View.GONE);
                playlistVoteDownButton.setVisibility(View.GONE);
            }
        });

        //Bewertungsmethode für positive Bewertung an den Moderator (Daumen hoch)
        moderatorVoteUpButton = (Button) findViewById(R.id.buttonModeratorVoteUp);
        builder = new AlertDialog.Builder(this);
        moderatorVoteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Danke für dein Feedback", Toast.LENGTH_SHORT).show();
                moderatorVoteUpButton.setVisibility(View.GONE);
                moderatorVoteDownButton.setVisibility(View.GONE);
                sendePositiveModeratorBewertung(); //die Funktion zum Versenden der Bewertung per SMS wird aufgerufen
            }
        });

        //Bewertungsmethode für negative Bewertung an den Moderator (Daumen hoch)
        moderatorVoteDownButton = (Button) findViewById(R.id.buttonModeratorVoteDown);
        builder = new AlertDialog.Builder(this);
        moderatorVoteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Danke für dein Feedback", Toast.LENGTH_SHORT).show();
                moderatorVoteUpButton.setVisibility(View.GONE);
                moderatorVoteDownButton.setVisibility(View.GONE);
                sendeNegativeModeratorBewertung(); //die Funktion zum Versenden der Bewertung per SMS wird aufgerufen
            }
        });

        //setze MediaPlayer zurück und initialisiere ihn neu
        createMediaplayer();
        //verstecke bei Start der App die Voting-Möglichkeiten für Playlist und Moderator
        playlistVoteUpButton.setVisibility(View.GONE);
        playlistVoteDownButton.setVisibility(View.GONE);
        moderatorVoteUpButton.setVisibility(View.GONE);
        moderatorVoteDownButton.setVisibility(View.GONE);
        //setze wichtige Variablen für die angezeigten Elemente und zur weiteren Verarbeitung
        titelElement = (TextView) findViewById(R.id.textAktuellTitelValue);
        interpretElement = (TextView) findViewById(R.id.textAktuellInterpretValue);
        albumElement = (TextView) findViewById(R.id.textAktuellAlbumValue);
        jahrElement = (TextView) findViewById(R.id.textAktuellJahrValue);
        playlistElement = (TextView) findViewById(R.id.textAktuellPlaylistValue);
        moderatorElement = (TextView) findViewById(R.id.textAktuellModeratorValue);
        playButton = (Button) findViewById(R.id.buttonPlay);
        builder = new AlertDialog.Builder(this);
        //Im Folgenden werden die Aktionen beim antippen des "Play"-Buttons aufgeführt
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prüfe, ob der MediaPlayer möglicherweise noch läuft (dürfte jedoch bei der Anzeige des Play-Buttons im Normalfall nicht passieren)
                if (soundFile.isPlaying()) {
                    //setze MediaPlayer zurück und initialisiere ihn neu
                    soundFile.reset();
                    createMediaplayer();
                    //setze alle Song-Angaben sowie die Voting-Möglichkeiten zurück
                    titelElement.setText("");
                    interpretElement.setText("");
                    albumElement.setText("");
                    jahrElement.setText("");
                    playlistElement.setText("");
                    moderatorElement.setText("");
                    playlistVoteUpButton.setVisibility(View.GONE);
                    playlistVoteDownButton.setVisibility(View.GONE);
                    moderatorVoteUpButton.setVisibility(View.GONE);
                    moderatorVoteDownButton.setVisibility(View.GONE);
                }
                //zeige dem Nutzer 3 Sekunden lang (bzw. später für die Dauer der Ladezeit) visuell an, dass der Audio-Stream bzw. die Audio-Datei geladen wird (Buffering...)
                soundFileLadestatus = new ProgressDialog(MainActivity.this);
                soundFileLadestatus.setMessage("Buffering...");
                soundFileLadestatus.show();
                CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //während der 3 Sekunden passiert im Hintergrund derzeit nichts
                        //später wird hier das Laden des Streams implementiert
                    }
                    public void onFinish() {
                        //nach Ablauf der 3 Sekunden (bzw. später nach dem Laden des Audio-Streams) wird ein "Playing..."-Hinweis angezeigt und der Stream gestartet
                        soundFileLadestatus.setMessage("Playing...");
                        soundFile.start();
                        //zeige alle Song-Angaben an und ermögliche das Voting für Playlist und Moderator
                        titelElement.setText("Test-Audio");
                        interpretElement.setText("Tobias Wichmann");
                        albumElement.setText("1. Album");
                        jahrElement.setText("2020");
                        playlistElement.setText("Mobile Software Engineering");
                        moderatorElement.setText("Max Mustermann");
                        playlistVoteUpButton.setVisibility(View.VISIBLE);
                        playlistVoteDownButton.setVisibility(View.VISIBLE);
                        moderatorVoteUpButton.setVisibility(View.VISIBLE);
                        moderatorVoteDownButton.setVisibility(View.VISIBLE);
                        //dieser Timer zeigt den "Playing-Hinweis" für zwei Sekunden an, danach wird er entfernt
                        CountDownTimer countDownTimer2 = new CountDownTimer(2000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                soundFileLadestatus.dismiss();
                            }
                        }.start();
                    }
                }.start();
                //zeige den Stop-Button an und verstecke den Play-Button
                playButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        //Im Folgenden werden die Aktionen beim antippen des "Stop"-Buttons aufgeführt
        stopButton = (Button) findViewById(R.id.buttonStop);
        stopButton.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(this);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prüfe, ob der MediaPlayer möglicherweise noch läuft
                if (soundFile.isPlaying()) {
                    //setze MediaPlayer zurück und initialisiere ihn neu
                    soundFile.reset();
                    createMediaplayer();
                    //setze alle Song-Angaben sowie die Voting-Möglichkeiten zurück
                    titelElement.setText("");
                    interpretElement.setText("");
                    albumElement.setText("");
                    jahrElement.setText("");
                    playlistElement.setText("");
                    moderatorElement.setText("");
                    playlistVoteUpButton.setVisibility(View.GONE);
                    playlistVoteDownButton.setVisibility(View.GONE);
                    moderatorVoteUpButton.setVisibility(View.GONE);
                    moderatorVoteDownButton.setVisibility(View.GONE);
                }
                //zeige den Play-Button an und verstecke den Stop-Button
                stopButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_SHORT).show(); //Info-Text: STOP
            }
        });

        //Im Folgenden werden die Aktionen beim antippen des "Musikwunsch"-Buttons aufgeführt
        wunschButton = (Button) findViewById(R.id.buttonWunsch);
        builder = new AlertDialog.Builder(this);
        wunschButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Anzeige der Aktivity "WunschActivity"
                Intent wunschAktivityIntent = new Intent(MainActivity.this, WunschActivity.class);
                startActivity(wunschAktivityIntent);
            }
        });
    }

    //Im Folgenden wird der MediaPlayer erzeugt und die Audio-Datei hinterlegt
    public void createMediaplayer() {
        soundFile = new MediaPlayer();
        soundFile = MediaPlayer.create(this, R.raw.testaudio);
    }

    //Im Folgenden wird eine SMS mit dem "Daumen-hoch"-Symbol als Inhalt sowie der Telefonnummer des Radios erzeugt
    public void sendePositiveModeratorBewertung()
    {
        Button up = (Button) moderatorVoteUpButton;
        Uri uri = Uri.parse(aktuelleModeratorNummer);
        Intent positiveBewertung = new Intent(Intent.ACTION_SENDTO, uri);
        positiveBewertung.putExtra("sms_body", up.getText().toString());
        startActivity(positiveBewertung);
    }

    //Im Folgenden wird eine SMS mit dem "Daumen-runter"-Symbol als Inhalt sowie der Telefonnummer des Radios erzeugt
    public void sendeNegativeModeratorBewertung()
    {
        Button down = (Button) moderatorVoteDownButton;
        Uri uri = Uri.parse(aktuelleModeratorNummer);
        Intent negativeBewertung = new Intent(Intent.ACTION_SENDTO, uri);
        negativeBewertung.putExtra("sms_body", down.getText().toString());
        startActivity(negativeBewertung);
    }
}