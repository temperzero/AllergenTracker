package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    TextView info, s_info, s_causes, b_text1, b_text2 ,b_text3, b_text4, b_text5, b_text6;
    TextView d_header, d_header2, b_doctor1, b_doctor2, b_doctor3 ,p_text, b_prec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("מידע כללי");

        // text
        info = (TextView)findViewById(R.id.FoodAllergiesInfo);
        s_info = (TextView)findViewById(R.id.SymptomsInfo);
        s_causes = (TextView)findViewById(R.id.SymptomCausesText);
        b_text1 = (TextView)findViewById(R.id.bullet1text);
        b_text2 = (TextView)findViewById(R.id.bullet2text);
        b_text3 = (TextView)findViewById(R.id.bullet3text);
        b_text4 = (TextView)findViewById(R.id.bullet4text);
        b_text5 = (TextView)findViewById(R.id.bullet5text);
        b_text6 = (TextView)findViewById(R.id.bullet6text);
        d_header = (TextView)findViewById(R.id.DoctorHeader);
        d_header2 = (TextView)findViewById(R.id.DoctorHeader2);
        b_doctor1 = (TextView)findViewById(R.id.doctorbullet1);
        b_doctor2 = (TextView)findViewById(R.id.doctorbullet2);
        b_doctor3 = (TextView)findViewById(R.id.Doctorbullet3);
        p_text = (TextView)findViewById(R.id.PrecautionText);
        b_prec = (TextView)findViewById(R.id.precautionbullets);

        info.setText("אלרגיית מזון היא תגובה של המערכת החיסונית המופיעה לאחר אכילת מזון מסויים שאפילו כמות קטנה של צריכתו גורמת לתסמינים כגון בעיות עיכול, פריחה או נפיחות בדרכי הנשימה.\n" +
                "לחלק מהאנשים, אלרגיה למזון יכולה לגרום לתסמינים חריפים יותר ולתגובות מסכנות חיים. \n");
        s_info.setText("עבור חלק מהאנשים תגובה אלרגית למזון מסויים יכולה לגרום לאי נוחות אך לא חמורה, " +
                "ולחלקם תגובה אלרגית כזו עלולה להיות הרבה יותר מפחידה ומסכנת חיים.\n" +
                "התסמינים בדרך כלל מתפתחים בטווח של כמה דקות ועד לשעתיים אחרי אכילת המזון. בפעמים נדירות יותר, התסמינים יתפתחו תוך כמה שעות.   \n");
        s_causes.setText("התסמינים הנפוצים ביותר לתגובה אלרית למזון כוללים:");
        b_text1.setText("\u2022 עקצוץ או גירוד בפה" );
        b_text2.setText("\u2022 פריחה גירוד או אקזמה" );
        b_text3.setText("\u2022 נפיחות של השפתיים, הפנים, הלשון והגרון או חלקים אחרים בגוף" );
        b_text4.setText("\u2022 צפצופים, נזלת, גודש או קשיי נשימה" );
        b_text5.setText("\u2022 כאבי בטן, שלשולים, בחילות או הקאות" );
        b_text6.setText("\u2022 סחרחורות או עלפון\n" );
        d_header.setText("גשו לרופא או אלרגולוג קופת החולים שלכם, במידה ויש לכם תסמינים של אלרגיה למזון זמן קצר לאחר צריכת האוכל. אם אפשר, פנו לקופת החולים שלכם כאשר התגובה האלרגית מתרחשת לצורך ביצוע אבחנה.");
        d_header2.setText("חפשו טיפול חירום אם אתם מפתחים סימנים או תסמינים של אנפילקסיס כגון:");
        b_doctor1.setText("\u2022 היצרות של דרכי הנשימה המקשה על הנשימה");
        b_doctor2.setText("\u2022 הלם עם ירידה חמורה בלחץ הדם");
        b_doctor3.setText("\u2022 סחרחורת" + "\n\u2022 דופק מהיר"+"\n");
        p_text.setText("ברגע שאלרגיה למזון כבר התפתחה, הדרך הטובה ביותר למניעת תגובה אלרגית היא להכיר ולהימנע ממזונות שגורמים לתסמינים. כמו כן, מזונות מסוימים משומשים כמרכיבים במנות מסויימות עשויים להיות מוסתרים היטב. זה נכון במיוחד במסעדות ובמסגרות חברתיות אחרות. אם אתם יודעים שיש לכם אלרגיה למזון, בצעו את השלבים הבאים:");
        b_prec.setText("\u2022 דעו מה אתם אוכלים ושותים. קראו את תוויות המזון בזהירות\n");
        b_prec.append("\u2022 אם כבר הייתה לכם תגובה חריפה בעבר, תלבשו צמיד רפואי אשר מאפשר לאחרים לדעת שיש לכם אלרגיה למזון, למקרה שתהיה לכם תגובה אלרגית ותהיה לכם בעיית תקשורת\n");
        b_prec.append("\u2022 דברו עם הרופא שלכם לגבי מתן מרשם חירום לאדרנלין. יכול להיות שתצטרכו להצטייד במזרק אם אתם בסיכון להתקף אלרגי חריף.");
        b_prec.append("\u2022 היו זהירים במסעדות, תהיו בטוחים שהמלצר או השף מודעים  לכך שאתם לחלוטין לא יכולים לאכול את המנות שאתם אלרגיים אליהם. כמו כן אתם צריכים להיות בטוחים לחלוטין שהארוכה שאתם מזמינים לא מכילה אלרגניים בעייתיים עבורכם. כמו כן, וודאו כי שהאוכל אינו מוכן על גבי משטחים או כלים שהכילו כל סוג שהוא של אוכל שאתם אלרגיים אליו\n");
        b_prec.append("\u2022 תכננו ארוחות וחטיפים לפני היציאה מהבית. במידת הצורך, קחו צידנית עמוסה במזון נטול אלרגנים כשאתם נוסעים או הולכים לאירוע. אם אתם או ילדכם לא יכולים לקבל את העוגה או הקינוח במסיבה, הביאו פינוק מיוחד מאושר כדי שאף אחד לא ירגיש מחוץ לחגיגה\n");


        TextView Website = (TextView)findViewById(R.id.Website);
        Website.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // back button enabled
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}