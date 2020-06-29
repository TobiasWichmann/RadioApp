package de.twproject.iubhradio;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

public class WunschActivity extends AppCompatActivity {

    TextView captionMusikwunschText;
    TextView erlaeuterungsText;
    TextView doneText;
    TextView eingegebenerText;
    String musikwunschText;
    Button abschickenButton;
    Button zurueckButton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wunsch);

        //setze wichtige Variablen für die angezeigten Elemente und zur weiteren Verarbeitung
        captionMusikwunschText = (TextView) findViewById(R.id.textCaptionMusikwunsch);
        erlaeuterungsText = (TextView) findViewById(R.id.textErlaeuterung);
        doneText = (TextView) findViewById(R.id.textDone);
        abschickenButton = (Button) findViewById(R.id.buttonAbschicken);
        zurueckButton = (Button) findViewById(R.id.buttonZurueck);
        builder = new AlertDialog.Builder(this);
        //blende die Elemente, die erst nach dem Abschicken des Musikwunsches angezeigt werden sollen, aus, und blende den Abschicken-Button ein
        abschickenButton.setVisibility(View.VISIBLE);
        doneText.setVisibility(View.GONE);

        //Im Folgenden werden die Aktionen beim antippen des "Musikwunsch übermitteln"-Buttons aufgeführt
        abschickenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //der eingegebene Text wird ausgelesen und an eine Funktion weitergeleitet, die eine E-Mail mit dem Musikwunsch erstellt
                eingegebenerText = (TextView) findViewById(R.id.textMusikwunsch);
                musikwunschText = eingegebenerText.getText().toString();
                sendeMusikwunsch();
                //verstecke die vorherigen Elemente (außer "zurück zur Übersicht") und blende einen Fertig-Hinweis ein
                doneText.setVisibility(View.VISIBLE);
                abschickenButton.setVisibility(View.GONE);
                eingegebenerText.setVisibility(View.GONE);
                erlaeuterungsText.setVisibility(View.GONE);
                captionMusikwunschText.setVisibility(View.GONE);
            }
        });

        //Im Folgenden werden die Aktionen beim antippen des "zurück zur Übersicht"-Buttons aufgeführt
        zurueckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Anzeige der Aktivity "MainActivity"
                Intent mainAktivityIntent = new Intent(WunschActivity.this, MainActivity.class);
                startActivity(mainAktivityIntent);
            }
        });
    }

    //Im Folgenden wird eine E-Mail mit dem eingegebenen Text als Inhalt sowie der E-Mail-Adresse des Radios als Empfänger erzeugt
    public void sendeMusikwunsch() {
        //lege Empfänger und Inhalt fest
        String[] recipients = new String[]{"wuensche@iubh-radio.de"};
        String subject = "Ein neuer Musikwunsch";
        String content = musikwunschText;
        Intent intentEmail = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intentEmail.putExtra(Intent.EXTRA_EMAIL, recipients);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentEmail.putExtra(Intent.EXTRA_TEXT, content);
        intentEmail.setType("text/plain");
        //Intent wird gestartet, der Nutzer kann ggf. die gewünsche E-Mail-App auswählen
        startActivity(Intent.createChooser(intentEmail, "Bitte wähle deine E-Mail-App aus"));
    }
}