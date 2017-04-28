package NoteItemView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cieo233 on 4/3/2017.
 */

public class NoteItemVIew extends RelativeLayout {
    private TextView title, date, content;
    private ImageView img1, img2, img3;
    private List<ImageView> imgs;
    private Note note;
    private float dp;
    private Context context;

    private static final String TAG = "NoteItemVIew";

    public NoteItemVIew(Context context) {
        this(context, null);
    }

    public NoteItemVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        addView(context);
    }

    void addView(Context context) {
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        title = new TextView(context);
        date = new TextView(context);
        content = new TextView(context);
        img1 = new ImageView(context);
        img2 = new ImageView(context);
        img3 = new ImageView(context);

        imgs = new ArrayList<>();
        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);

        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        title.setTextColor(Color.parseColor("#22262F"));
        title.setMaxLines(1);
        date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        date.setTextColor(Color.parseColor("#22262F"));
        date.setBackgroundResource(R.mipmap.underline);
        date.setMaxLines(1);
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        content.setTextColor(Color.parseColor("#9b9b9b"));

        title.setId(View.generateViewId());
        date.setId(View.generateViewId());
        content.setId(View.generateViewId());
        img1.setId(View.generateViewId());
        img2.setId(View.generateViewId());
        img3.setId(View.generateViewId());

        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
        addView(title, titleParams);

        LayoutParams dateParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.addRule(BELOW, title.getId());
        addView(date, dateParams);

        LayoutParams img1Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img1Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        img1Params.setMarginStart((int) (dp * 5));
        addView(img1,img1Params);

        LayoutParams img2Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img2Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        img2Params.addRule(END_OF,img1.getId());
        img2Params.setMarginStart((int) (dp * 5));
        addView(img2,img2Params);

        LayoutParams img3Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img3Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        img3Params.addRule(END_OF,img2.getId());
        img3Params.setMarginStart((int) (dp * 5));
        addView(img3,img3Params);

        LayoutParams contentParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(ABOVE,img1.getId());
        contentParams.addRule(BELOW,date.getId());
        addView(content,contentParams);
    }

    public void setNote(Note note){
        if (note == null){
            this.note = null;
            this.title.setText("");
            this.date.setText("");
            this.content.setText("");
            return;
        }
        this.note = note;
        findImg();
        this.title.setText(this.note.getTitle());
        this.date.setText(this.note.getDate());
        String content = this.note.getContent();

        if (content!=null){
            if (content.length() > 100){
                this.content.setText(content.substring(0,100));
            }else {
                this.content.setText(content);
            }
        }
        img1.setVisibility(GONE);
        img2.setVisibility(GONE);
        img3.setVisibility(GONE);
    }

    @Override
    public String toString(){
        String s = "";
        if (note != null){
            s = note.getTitle() + "    "+note.getDate();
            if (note.getContent() != null){
                s += "    " + note.getContent();
            }
        }
        return s;
    }

    private void findImg(){
        if (note != null && note.getContent() != null){
            String regex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
            String content = note.getContent();
            Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            int i = 0;
            while (matcher.find()){
                if (i == 3){
                    break;
                }
                imgs.get(i).setVisibility(VISIBLE);
                Glide.with(context).load(matcher.group(1)).into(imgs.get(i));
                i ++;
            }

        }
    }

}
