package NoteItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slidenote.www.slidenotev2.Model.Note;

import java.io.File;
import java.util.List;

/**
 * Created by Cieo233 on 4/3/2017.
 */

public class NoteItemVIew extends RelativeLayout {
    private TextView title, date, content;
    private ImageView img1, img2, img3;
    private List<ImageView> imgs;
    private float dp;

    public NoteItemVIew(Context context) {
        this(context, null);
    }

    public NoteItemVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        title.setId(View.generateViewId());
        date.setId(View.generateViewId());
        content.setId(View.generateViewId());
        img1.setId(View.generateViewId());
        img2.setId(View.generateViewId());
        img3.setId(View.generateViewId());

        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        addView(title, titleParams);

        LayoutParams dateParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.addRule(BELOW, title.getId());
        addView(date, dateParams);

        LayoutParams img1Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img1Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        addView(img1,img1Params);

        LayoutParams img2Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img2Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        img2Params.addRule(END_OF,img1.getId());
        addView(img2,img2Params);

        LayoutParams img3Params = new LayoutParams((int) (dp * 20), (int)(dp * 20));
        img3Params.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        img3Params.addRule(END_OF,img2.getId());
        addView(img3,img3Params);

        LayoutParams contentParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(ABOVE,img1.getId());
        contentParams.addRule(BELOW,date.getId());
        addView(content,contentParams);
    }

    public void setNote(Note note){
        title.setText(note.getTitle());
        date.setText(note.getDate());
        String content = note.getContent();
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

}
