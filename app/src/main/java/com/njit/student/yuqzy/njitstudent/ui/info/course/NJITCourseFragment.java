package com.njit.student.yuqzy.njitstudent.ui.info.course;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.model.PictureConfig;
import com.njit.student.yuqzy.njitstudent.Event.CourseEvent;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.Cell;
import com.njit.student.yuqzy.njitstudent.database.CourseDatabase;
import com.njit.student.yuqzy.njitstudent.database.formKBdatabase;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.database.formSJKdatabase;
import com.njit.student.yuqzy.njitstudent.database.formTTBinfoDatabase;
import com.njit.student.yuqzy.njitstudent.model.FormSJK;
import com.njit.student.yuqzy.njitstudent.model.FormSJKCategory;
import com.njit.student.yuqzy.njitstudent.model.FormTTBCategory;
import com.njit.student.yuqzy.njitstudent.model.FormTTBinfo;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ChooseTypeAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.CourseDialogAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ThemeUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.yalantis.ucrop.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_COURSE_STO_OK;

public class NJITCourseFragment extends Fragment implements View.OnClickListener {
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private Toolbar mToolbar;
    private LinearLayout course_layout;
    private ImageView imgMore;
    private TextView tvTitle;
    int baseHeight = 0;
    int baseWidth = 0;
    private Realm realm;
    private SimpleDateFormat dateFormatPattern;

    public NJITCourseFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Realm.init(getContext());
    }

    ViewGroup con;
    CourseDatabase courseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_njitcourse, container, false);
        courseDatabase = (CourseDatabase) getArguments().getSerializable("course");
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        course_layout = (LinearLayout) view.findViewById(R.id.course_layout);
        if (SettingsUtil.getCourseBacGround() != "") {
            setBackground(SettingsUtil.getCourseBacGround());
        }
        mToolbar.setTitle("我的课表");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(SettingsUtil.getUserCourseTerm() + " 第" + SettingsUtil.getCurrentWeek() + "周");
        imgMore = (ImageView) view.findViewById(R.id.img_more);
        imgMore.setOnClickListener(this);
        view.findViewById(R.id.img_change_type).setOnClickListener(this);
        baseHeight = getContext().getResources().getDisplayMetrics().heightPixels / 11;
        int cell = getContext().getResources().getDisplayMetrics().widthPixels / 15;
        baseWidth = cell * 2;
        init_view(view);

        if (MainActivity.network != null && MainActivity.network.cookieStore != null) {
            network = MainActivity.network;
        } else {
            network = new ZfNetData(getContext());
        }

        // 更新学期课表视图
        ViewGroup seq = (ViewGroup) view.findViewById(R.id.week_class_seq);
        con = (ViewGroup) view.findViewById(R.id.week_class_content);
        addSeqViews(seq);
        createView(con, genCellData(courseDatabase, SettingsUtil.getUserCourseType()));
        return view;
    }

    private void init_current_week() {
        Calendar precalendar = Calendar.getInstance();//获得一个日历的实例
        dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        Date date = null;
        if (SettingsUtil.getCourseCurrentWeekBindTime() != "") {
            try {
                date = dateFormatPattern.parse(SettingsUtil.getCourseCurrentWeekBindTime());//初始日期
            } catch (Exception e) {

            }
            precalendar.setTime(date);
        }
        Calendar calendar = Calendar.getInstance();
        SettingsUtil.setCurrentWeek(Integer.toString(calendar.get(Calendar.WEEK_OF_YEAR) - precalendar.get(Calendar.WEEK_OF_YEAR)) + SettingsUtil.getCourseStartWeek());
        Log.e("week", precalendar.get(Calendar.WEEK_OF_YEAR) + " vs " + calendar.get(Calendar.WEEK_OF_YEAR));
    }

    public static String getCurrentTime() {//2017-2-19
        SimpleDateFormat dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        long time = Calendar.getInstance().getTimeInMillis();
        return dateFormatPattern.format(time);
    }

    private void init_view(View view) {
        Time time = new Time("GMT+8");
        time.setToNow();
        ((TextView) view.findViewById(R.id.show_year)).setText(Integer.toString(time.year));
        ((TextView) view.findViewById(R.id.show_month)).setText((time.month + 1) + "月");
        int weekday = time.weekDay;
        int monthday = time.monthDay;
        String[] value;
        switch (weekday) {
            case 0:
                (view.findViewById(R.id.ll_week_7)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 1);
                value[1] = Integer.toString(monthday);
                value[2] = Integer.toString(monthday + 1);
                value[3] = Integer.toString(monthday + 2);
                value[4] = Integer.toString(monthday + 3);
                value[5] = Integer.toString(monthday + 4);
                value[6] = Integer.toString(monthday + 5);
                init_header(view, value);
                break;
            case 1:
                (view.findViewById(R.id.ll_week_1)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 2);
                value[1] = Integer.toString(monthday - 1);
                value[2] = Integer.toString(monthday);
                value[3] = Integer.toString(monthday + 1);
                value[4] = Integer.toString(monthday + 2);
                value[5] = Integer.toString(monthday + 3);
                value[6] = Integer.toString(monthday + 4);
                init_header(view, value);
                break;
            case 2:
                (view.findViewById(R.id.ll_week_2)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 3);
                value[1] = Integer.toString(monthday - 2);
                value[2] = Integer.toString(monthday - 1);
                value[3] = Integer.toString(monthday);
                value[4] = Integer.toString(monthday + 1);
                value[5] = Integer.toString(monthday + 2);
                value[6] = Integer.toString(monthday + 3);
                init_header(view, value);
                break;
            case 3:
                (view.findViewById(R.id.ll_week_3)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 4);
                value[1] = Integer.toString(monthday - 3);
                value[2] = Integer.toString(monthday - 2);
                value[3] = Integer.toString(monthday - 1);
                value[4] = Integer.toString(monthday);
                value[5] = Integer.toString(monthday + 1);
                value[6] = Integer.toString(monthday + 2);
                init_header(view, value);
                break;
            case 4:
                (view.findViewById(R.id.ll_week_4)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 5);
                value[1] = Integer.toString(monthday - 4);
                value[2] = Integer.toString(monthday - 3);
                value[3] = Integer.toString(monthday - 2);
                value[4] = Integer.toString(monthday - 1);
                value[5] = Integer.toString(monthday);
                value[6] = Integer.toString(monthday + 1);
                init_header(view, value);
                break;
            case 5:
                (view.findViewById(R.id.ll_week_5)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday - 6);
                value[1] = Integer.toString(monthday - 5);
                value[2] = Integer.toString(monthday - 4);
                value[3] = Integer.toString(monthday - 3);
                value[4] = Integer.toString(monthday - 2);
                value[5] = Integer.toString(monthday - 1);
                value[6] = Integer.toString(monthday);
                init_header(view, value);
                break;
            case 6:
                (view.findViewById(R.id.ll_week_6)).setBackgroundResource(R.drawable.today_course_bac);
                value = new String[7];
                value[0] = Integer.toString(monthday);
                value[1] = Integer.toString(monthday + 1);
                value[2] = Integer.toString(monthday + 2);
                value[3] = Integer.toString(monthday + 3);
                value[4] = Integer.toString(monthday + 4);
                value[5] = Integer.toString(monthday + 5);
                value[6] = Integer.toString(monthday + 6);
                init_header(view, value);
                break;
        }
    }

    private void init_header(View view, String[] value) {
        ((TextView) view.findViewById(R.id.day_1)).setText(value[0]);
        ((TextView) view.findViewById(R.id.day_2)).setText(value[1]);
        ((TextView) view.findViewById(R.id.day_3)).setText(value[2]);
        ((TextView) view.findViewById(R.id.day_4)).setText(value[3]);
        ((TextView) view.findViewById(R.id.day_5)).setText(value[4]);
        ((TextView) view.findViewById(R.id.day_6)).setText(value[5]);
        ((TextView) view.findViewById(R.id.day_7)).setText(value[6]);
    }

    private void addSeqViews(ViewGroup index) {
        if (index != null) {
            index.removeAllViews();

            for (int i = 1; i <= 11; i++) {
                TextView textView = new TextView(getContext());
                textView.setText("" + i);
                textView.setGravity(Gravity.CENTER);
                textView.setWidth(index.getWidth());
                textView.setHeight(baseHeight);
                textView.setBackgroundResource(R.drawable.normal_course_bac);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTextSize(10);
                index.addView(textView);
            }
        }
    }

    private int[] bac = new int[]{R.drawable.class_bac_1, R.drawable.class_bac_2, R.drawable.class_bac_3, R.drawable.class_bac_4, R.drawable.class_bac_5, R.drawable.class_bac_6, R.drawable.class_bac_7};
    private int[] bac_more = new int[]{R.drawable.class_bac_more_1, R.drawable.class_bac_more_2, R.drawable.class_bac_more_3, R.drawable.class_bac_more_4, R.drawable.class_bac_more_5, R.drawable.class_bac_more_6, R.drawable.class_bac_more_7};
    Random random = new Random(8);

    private void addContentView(final Cell cell, final ViewGroup index) {
        click = 0;
        final CardView card = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.item_class_info, null);
        card.setX((cell.getX() - 2) * baseWidth + 5);
        card.setY((cell.getY() - 2) * baseHeight + 5);
        final TextView textView = (TextView) card.findViewById(R.id.class_name);
        String info = (cell.getPlaceName().get(0) == null || cell.getPlaceName().get(0) == "") ? cell.getCourseName().get(0) : (cell.getCourseName().get(0) + "\n@" + cell.getPlaceName().get(0));
        textView.setText(info);
        textView.setWidth(baseWidth - 24);
        textView.setHeight(baseHeight * cell.getLength() - 24);
        Log.e("week cell", cell.getWeek().get(0) + "");
        //
        if (cell.getCourseName() != null && cell.getCourseName().size() > 1) {
            if (cell.getWeek() != null && cell.getWeek().get(0) != null && cell.getWeek().get(0).contains(Integer.parseInt(SettingsUtil.getCurrentWeek()))) {
                card.setBackgroundResource(bac_more[random.nextInt(7)]);
            } else {
                for (int i = 0; i < cell.getCourseName().size(); i++) {
                    if (cell.getWeek().get(i).contains(Integer.parseInt(SettingsUtil.getCurrentWeek()))) {
                        card.setBackgroundResource(bac_more[random.nextInt(7)]);
                        info = (cell.getPlaceName().get(i) == null || cell.getPlaceName().get(i) == "") ? cell.getCourseName().get(i) : (cell.getCourseName().get(i) + "\n@" + cell.getPlaceName().get(i));
                        textView.setText(info);
                    } else {
                        card.setBackgroundResource(R.drawable.class_bac_grey_more);
                    }
                }

            }
        } else {
            if (cell.getWeek() != null && cell.getWeek().get(0) != null && cell.getWeek().get(0).contains(Integer.parseInt(SettingsUtil.getCurrentWeek()))) {
                card.setBackgroundResource(bac[random.nextInt(7)]);
            } else {
                card.setBackgroundResource(R.drawable.class_bac_grey);
            }
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                intent.putExtra("data", cell);
                startActivity(intent);

            }
        });
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (cell.getCourseName() != null && cell.getCourseName().size() > 1) {

                    click++;
                    if (!(click < cell.getCourseName().size())) {
                        click = 0;
                    }
                    String info = (cell.getPlaceName().get(click) == null || cell.getPlaceName().get(click) == "") ? cell.getCourseName().get(click) : (cell.getCourseName().get(click) + "\n@" + cell.getPlaceName().get(click));
                    textView.setText(info);
                    if (cell.getWeek().get(click).contains(Integer.parseInt(SettingsUtil.getCurrentWeek()))) {
                        card.setBackgroundResource(bac_more[random.nextInt(7)]);
                    } else {
                        card.setBackgroundResource(R.drawable.class_bac_grey_more);
                    }

                } else {
                    Toast.makeText(getContext(), "无法切换课程", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        index.addView(card);
    }

    int click = 0;

    private void createView(ViewGroup index, List<List<Cell>> data) {
        init_current_week();
        tvTitle.setText(SettingsUtil.getUserCourseTerm() + "\n 第" + SettingsUtil.getCurrentWeek() + "周");
        if (index != null) {
            index.removeAllViews();
            for (int i = 0; i < 11; i++) {
                List<Cell> currentRow = data.get(i);
                for (int j = 0; j < 7; j++) {
                    Cell cell = currentRow.get(j);
                    int length = cell.getLength();
                    if (length > 1) {
                        //判断当前课程是否为第一个
                        if (i > 0) {
                            //判断此节课的上一节课是否与之相同
                            Cell cell1 = data.get(i - 1).get(j);
                            if (cell.getCourseName().equals(cell1.getCourseName()) && cell.getPlaceName().equals(cell1.getPlaceName())) {
                                //不添加这节课
                            } else {
                                if (cell.getCourseName() != null && cell.getCourseName().size() > 0 && cell.getCourseName().get(0) != "") {
                                    addContentView(cell, index);
                                }
                            }
                        } else {
                            if (cell.getCourseName() != null && cell.getCourseName().size() > 0 && cell.getCourseName().get(0) != "") {
                                addContentView(cell, index);
                            }
                        }
                    } else {
                        if (cell.getCourseName() != null && cell.getCourseName().size() > 0 && cell.getCourseName().get(0) != "") {
                            addContentView(cell, index);
                        }
                    }

                }
            }
        }

    }

    private List<List<Cell>> genCellData(CourseDatabase courseDatabase, String type) {
        RealmList<formKBdatabase> kbList = courseDatabase.getFormKBdatabases();
        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < 11; i++) {

            List<formKBdatabase> currentRow = new ArrayList<>();
            for (int n = 0; n < kbList.size(); n++) {
                formKBdatabase kb = kbList.get(n);
                //Log.e("do kb", kb.getText());
                if (kb.getRowspan() - 2 == i) {
                    currentRow.add(kb);
                }
            }
            //排序
            for (int k = 0; k < currentRow.size() - 1; k++) {
                for (int m = 0; m < currentRow.size() - 1 - k; m++) {
                    formKBdatabase kb1 = currentRow.get(m);
                    formKBdatabase kb2 = currentRow.get(m + 1);
                    if (kb1.getColspan() > kb2.getColspan()) {
                        currentRow.set(m, kb2);
                        currentRow.set(m + 1, kb1);
                    }
                }
            }

            List<Cell> cellList = new ArrayList<>();
            cells.add(cellList);
            for (int j = 0; j < 7; j++) {
                Cell cell = new Cell();
                formKBdatabase row = currentRow.get(j + 2);
                cell.setLength(row.getLength());
                cell.setX(row.getColspan());
                cell.setY(row.getRowspan());
                String[] arr = row.getText().trim().split("\\s+");

                List<String> course = new ArrayList<>();
                List<String> courseType = new ArrayList<>();
                List<String> teacher = new ArrayList<>();
                List<String> place = new ArrayList<>();
                List<String> courseWeek = new ArrayList<>();
                List<String> changeCode = new ArrayList<>();
                List<List<Integer>> weeks = new ArrayList<>();

                for (int k = 0; k < arr.length; k++) {
                    cell.setOriginText(arr);
                    String s = arr[k];
                    switch (type) {
                        case "class":
                            if (s.contains("(") && s.contains(")") && s.contains(",")) {
                                courseWeek.add(s);

                                String[] info = null;
                                String w = s.substring(0, s.indexOf("("));
                                List<Integer> week = new ArrayList<>();
                                if (w.contains(",")) {
                                    info = w.split(",");
                                } else {
                                    info = new String[]{w};
                                }
                                int startweek;
                                int endweek;

                                for (int f = 0; f < info.length; f++) {
                                    String cur = info[f];
                                    try {
                                        if (cur.contains("-")) {
                                            startweek = Integer.parseInt(cur.substring(0, cur.indexOf("-")));
                                            endweek = Integer.parseInt(cur.substring(cur.indexOf("-") + 1, cur.length()));
                                            if (startweek <= endweek) {
                                                for (int g = startweek; g <= endweek; g++) {
                                                    week.add(g);
                                                }
                                            }
                                        } else {
                                            week.add(Integer.parseInt(cur));
                                        }
                                    } catch (NumberFormatException e) {
                                    }
                                }
                                weeks.add(week);


                                if (k >= 2 && k + 2 < arr.length) {
                                    course.add(arr[k - 2]);
                                    courseType.add(arr[k - 1]);
                                    teacher.add(arr[k + 1]);
                                    place.add(arr[k + 2]);
                                }
                                if ((k + 3) < arr.length && !arr[k + 3].contains("调") && !s.contains(")") && !s.contains("(")) {
                                    changeCode.add(arr[k + 3]);
                                } else {
                                    changeCode.add("");
                                }


                            } else if (s.contains("节/周")) {
                                course.add(arr[k - 1]);
                                courseType.add("");
                                teacher.add("");
                                place.add("");
                                changeCode.add("");
                                weeks.add(null);
                                courseWeek.add(s);
                            }

                            break;
                        case "person":
                            if (s.contains("第") && s.contains(",") && s.contains("节") && s.contains("{") && s.contains("}")) {
                                courseWeek.add(s);
                                //周二第3,4节{第1-5周|单周}
                                String w = s.substring(s.indexOf("{") + 1, s.indexOf("}"));
                                List<Integer> week = new ArrayList<>();

                                int startweek;
                                int endweek;
                                int classType;//0 正常  1 单周  2 双周

                                try {
                                    if (w.contains("-")) {
                                        startweek = Integer.parseInt(w.substring(1, w.indexOf("-")));
                                        endweek = Integer.parseInt(w.substring(w.indexOf("-") + 1, w.indexOf("周")));

                                        classType = 0;
                                        Log.e("week",w);
                                        if (w.contains("单周")) {
                                            classType = 1;
                                        } else if (w.contains("双周")) {
                                            classType = 2;
                                        }

                                        for (int g = startweek; g <= endweek; g++) {

                                            if (classType == 1 && g % 2 == 1) {
                                                Log.e("do week","class type 1");
                                                week.add(g);
                                            } else if (classType == 2 && g % 2 == 0) {
                                                Log.e("do week","class type 2"+g);
                                                week.add(g);
                                            } else if(classType==0){
                                                Log.e("do week","class type 0"+g);
                                                week.add(g);
                                            }

                                        }
                                    } else {

                                    }
                                } catch (NumberFormatException e) {
                                }

                                weeks.add(week);


                                if (k >= 2 && k + 2 < arr.length) {
                                    course.add(arr[k - 2]);
                                    courseType.add(arr[k - 1]);
                                    teacher.add(arr[k + 1]);
                                    place.add(arr[k + 2]);
                                }
                                if ((k + 3) < arr.length && !arr[k + 3].contains("调") && !s.contains(")") && !s.contains("(")) {
                                    changeCode.add(arr[k + 3]);
                                } else {
                                    changeCode.add("");
                                }


                            }
                            break;
                    }

                    cell.setCourseName(course);
                    cell.setCourseType(courseType);
                    cell.setCourseWeek(courseWeek);
                    cell.setPlaceName(place);
                    cell.setTeacher(teacher);
                    cell.setCourseChangeCode(changeCode);
                    cell.setWeek(weeks);
                }

                cellList.add(cell);
            }
        }
        return cells;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_more:
                displayDialog();
                break;
            case R.id.img_change_type:
                chooseCourseType();
                break;
        }
    }

    private String[] chooseTerm = new String[2];

    private void displayDialog() {

        if (SettingsUtil.getXueHao() != null && SettingsUtil.getUserCourseTerm() != "") {
            String[] info = SettingsUtil.getUserCourseTerm().split(":");
            String type = "";
            if (SettingsUtil.getUserCourseType().equals("class")) {
                type = "班级课表";
            } else {
                type = "学生个人课表";
            }
            final String[] current = new String[]{SettingsUtil.getXueHao(), info[0] + " 第" + info[1] + "学期", SettingsUtil.getCurrentWeek(), type, "", "", "", "", ""};
            CourseDialogAdapter adapter = new CourseDialogAdapter(getContext(), current);

            DialogPlus dialog = DialogPlus.newDialog(getContext())
                    .setBackgroundColorResId(R.color.transparent)
                    .setContentHolder(new ListHolder())
                    .setHeader(R.layout.course_dialog_header)
                    .setFooter(R.layout.course_dialog_footer)
                    .setCancelable(true)
                    .setAdapter(adapter)
                    .setGravity(Gravity.BOTTOM)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            switch (view.getId()) {
                                case R.id.footer_close_button:
                                    dialog.dismiss();
                                    break;
                                case R.id.footer_confirm_button:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            //final DialogPlus menuDialog=dialog;
                            Log.e("DialogPlus", "onItemClick() called with: " + "item = [" +
                                    item + "], position = [" + position + "]");
                            dialog.dismiss();
                            switch (position) {
                                case 0:
                                    dialogLogin = loginZfDialog("切换账户");
                                    dialogLogin.show();
                                    break;
                                case 1:
                                    chooseTermDialog();
                                    break;
                                case 2://周
                                    chooseTermWeek();
                                    break;
                                case 3://课表类型
                                    chooseType();
                                    break;
                                case 4://添加课程
                                    startActivity(new Intent(getContext(), AddCourseActivity.class));
                                    break;
                                case 5://实习课信息
                                    if (courseDatabase != null) {
                                        getSJK(courseDatabase);
                                    }
                                    break;
                                case 6://调课信息
                                    if (courseDatabase != null) {
                                        getTTB(courseDatabase);
                                    }
                                    break;
                                case 7://刷新信息
                                    if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null && MainActivity.network.theUrls != null) {
                                        if (SettingsUtil.getUserCourseType().equals("class")) {
                                            getBJCourse(MainActivity.network, MainActivity.personInfo, SettingsUtil.getUserCourseTerm().split(":"));
                                        } else {
                                            String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                            MainActivity.network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                        }
                                    } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                        if (SettingsUtil.getUserCourseType().equals("class")) {
                                            getBJCourse(network, personInfo, SettingsUtil.getUserCourseTerm().split(":"));
                                        } else {
                                            String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                            network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                        }
                                    } else {
                                        dialogLogin = loginZfDialog("重新登录");
                                        dialogLogin.show();
                                    }
                                    break;

                                case 8://更换背景
                                    chooseBac();
                                    break;
                            }
                        }
                    })
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setOverlayBackgroundResource(android.R.color.transparent)
                    .setExpanded(false)
                    .create();
            dialog.show();
        }
    }

    //从网络获取课表
    private void getBJCourse(ZfNetData network, PersonInfo personInfo, String[] chooseTerm) {
        String[] xy_name = getResources().getStringArray(R.array.xy_name);
        String[] xy_value = getResources().getStringArray(R.array.xy_name);
        int key = 0;
        String personXY = personInfo.getPersonXY();
        for (int i = 0; i < xy_name.length; i++) {
            if (xy_name[i].equals(personXY)) {
                key = i;
                break;
            }
        }
        network.getCourseForm(personInfo.getPersonXH(),
                personInfo.getPersonXZB(),
                chooseTerm[0], chooseTerm[1],
                personInfo.getPersonDQSZJ(),
                xy_value[key],
                personInfo.getPersonZYMC());// 0107 0501;
    }

    //学期选择
    private Dialog chooseTermDialog() {
        return new MaterialDialog.Builder(getContext())
                .title("选择学期")
                .items(R.array.term)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog2, View itemView, int which, CharSequence text) {

                        chooseTerm[0] = text.toString().substring(0, 9);
                        chooseTerm[1] = text.toString().substring(11, 12);
                        if (SettingsUtil.getXueHao() != "") {
                            courseDatabase = null;
                            courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), chooseTerm[0] + ":" + chooseTerm[1], SettingsUtil.getUserCourseType());
                            if (courseDatabase != null) {
                                SettingsUtil.setUserCourseTerm(chooseTerm[0] + ":" + chooseTerm[1]);
                                createView(con, genCellData(courseDatabase, SettingsUtil.getUserCourseType()));
                            } else if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null && MainActivity.network.theUrls != null) {
                                if (SettingsUtil.getUserCourseType().equals("class")) {
                                    getBJCourse(MainActivity.network, MainActivity.personInfo, chooseTerm);
                                } else {
                                    MainActivity.network.getPersonCourseForm(MainActivity.personInfo.getPersonXH(), chooseTerm[0], chooseTerm[1]);
                                }
                            } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                if (SettingsUtil.getUserCourseType().equals("class")) {
                                    getBJCourse(network, personInfo, chooseTerm);
                                } else {
                                    network.getPersonCourseForm(personInfo.getPersonXH(), chooseTerm[0], chooseTerm[1]);
                                }
                            } else {
                                dialogLogin = loginZfDialog("教务网登录");
                                dialogLogin.show();
                            }

                        } else {

                            dialogLogin = loginZfDialog("教务网登录");
                            dialogLogin.show();
                        }

                        return false;
                    }
                })
                .negativeText("取消")
                .show();
    }

    private Dialog chooseTermWeek() {
        return new MaterialDialog.Builder(getContext())
                .title("选择当前周")
                .items(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        SettingsUtil.setCurrentWeek(text.toString());
                        SettingsUtil.setCourseStartWeek(text.toString());
                        SettingsUtil.setCourseCurrentWeekBindTime(getCurrentTime());
                        createView(con, genCellData(courseDatabase, SettingsUtil.getUserCourseType()));

                    }
                })
                .negativeText("取消")
                .show();
    }

    private Dialog chooseType() {
        return new MaterialDialog.Builder(getContext())
                .title("选择课表类型")
                .items(new String[]{"班级课表", "学生个人课表"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text.toString().equals("班级课表")) {
                            if (SettingsUtil.getUserCourseType() != "class") {
                                courseDatabase = null;
                                courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), chooseTerm[0] + ":" + chooseTerm[1], "class");
                                if (courseDatabase != null) {
                                    SettingsUtil.setUserCourseType("class");
                                    createView(con, genCellData(courseDatabase, "class"));
                                } else if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null && MainActivity.network.theUrls != null) {
                                    getBJCourse(MainActivity.network, MainActivity.personInfo, chooseTerm);
                                } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                    getBJCourse(network, personInfo, chooseTerm);
                                } else {
                                    dialogLogin = loginZfDialog("教务网登录");
                                    dialogLogin.show();
                                }
                            }
                        } else if (text.toString().equals("学生个人课表")) {
                            if (SettingsUtil.getUserCourseType() != "person") {
                                courseDatabase = null;
                                courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserCourseTerm(), "person");
                                if (courseDatabase != null) {
                                    SettingsUtil.setUserCourseType("peson");
                                    createView(con, genCellData(courseDatabase, "person"));
                                } else {
                                    if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.network.theUrls != null) {
                                        String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                        network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                    } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                        String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                        network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                    } else {
                                        dialogLogin = loginZfDialog("教务网登录");
                                        dialogLogin.show();
                                    }
                                }
                            }
                        }

                    }
                })
                .negativeText("取消")
                .show();
    }

    private void chooseCourseType() {
        ChooseTypeAdapter adapter = new ChooseTypeAdapter(getContext());
        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(new GridHolder(2))
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.e("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                        switch (position) {
                            case 0:
                                if (!SettingsUtil.getUserCourseType().equals("class")) {

                                    courseDatabase = null;
                                    courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), chooseTerm[0] + ":" + chooseTerm[1], "class");
                                    if (courseDatabase != null) {
                                        SettingsUtil.setUserCourseType("class");
                                        createView(con, genCellData(courseDatabase, "class"));
                                    } else if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null && MainActivity.network.theUrls != null) {
                                        getBJCourse(MainActivity.network, MainActivity.personInfo, chooseTerm);
                                    } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                        getBJCourse(network, personInfo, chooseTerm);
                                    } else {
                                        dialogLogin = loginZfDialog("教务网登录");
                                        dialogLogin.show();
                                    }
                                }
                                break;
                            case 1:
                                if (!SettingsUtil.getUserCourseType().equals("person")) {

                                    courseDatabase = null;
                                    courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserCourseTerm(), "person");
                                    if (courseDatabase != null) {
                                        SettingsUtil.setUserCourseType("person");
                                        createView(con, genCellData(courseDatabase, "person"));
                                    } else {
                                        if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.network.theUrls != null) {
                                            String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                            network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                        } else if (network.cookieStore != null && personInfo != null && network.theUrls != null) {
                                            String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                            network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                                        } else {
                                            dialogLogin = loginZfDialog("教务网登录");
                                            dialogLogin.show();
                                        }
                                    }
                                }
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
    }


    //得到实习课信息
    private void getSJK(CourseDatabase courseDatabase) {
        RealmList<formSJKdatabase> sjkList = courseDatabase.getFormSJKdatabases();
        List<FormSJK> list = new ArrayList<>();
        for (formSJKdatabase data : sjkList) {
            FormSJK sjk = new FormSJK();
            sjk.setCourseName(data.getCourseName());
            sjk.setTeacher(data.getTeacher());
            sjk.setStudyTime(data.getStudyTime());
            sjk.setStudyPlace(data.getStudyPlace());
            sjk.setCredit(data.getCredit());
            sjk.setTime(data.getTime());
            list.add(sjk);
        }
        FormSJKCategory category = new FormSJKCategory();
        category.setSjkList(list);
        Intent intent = new Intent(getContext(), CourseSJKActivity.class);
        intent.putExtra("data", category);
        startActivity(intent);

    }

    //得到调\停\补课信息
    private void getTTB(CourseDatabase courseDatabase) {
        RealmList<formTTBinfoDatabase> sjkList = courseDatabase.getFormTTBinfoDatabases();
        List<FormTTBinfo> list = new ArrayList<>();
        for (formTTBinfoDatabase data : sjkList) {
            FormTTBinfo ttb = new FormTTBinfo();
            ttb.setCourseName(data.getCourseName());
            ttb.setId(data.getId());
            ttb.setPostTime(data.getPostTime());
            ttb.setPreStudyState(data.getPreStudyState());
            ttb.setNowStudyState(data.getNowStudyState());
            list.add(ttb);
        }
        FormTTBCategory category = new FormTTBCategory();
        category.setTtbList(list);
        Intent intent = new Intent(getContext(), CourseTTBActivity.class);
        intent.putExtra("data", category);
        startActivity(intent);

    }

    //选择背景图片
    private void chooseBac() {
        // 进入相册
        /**
         * type --> 1图片 or 2视频
         * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
         * maxSelectNum --> 可选择图片的数量
         * selectMode         --> 单选 or 多选
         * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
         * isPreview    --> 是否打开预览选项
         * isCrop       --> 是否打开剪切选项
         * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
         * ThemeStyle -->主题颜色
         * CheckedBoxDrawable -->图片勾选样式
         * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
         * cropH-->裁剪高度 值不能小于100
         * isCompress -->是否压缩图片
         * setEnablePixelCompress 是否启用像素压缩
         * setEnableQualityCompress 是否启用质量压缩
         * setRecordVideoSecond 录视频的秒数，默认不限制
         * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
         * setImageSpanCount -->每行显示个数
         * setCheckNumMode 是否显示QQ选择风格(带数字效果)
         * setPreviewColor 预览文字颜色
         * setCompleteColor 完成文字颜色
         * setPreviewBottomBgColor 预览界面底部背景色
         * setBottomBgColor 选择图片页面底部背景色
         * setCompressQuality 设置裁剪质量，默认无损裁剪
         * setSelectMedia 已选择的图片
         * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
         * 注意-->type为2时 设置isPreview or isCrop 无效
         * 注意：Options可以为空，默认标准模式
         */
        Log.e("test", "choose bac");
//        int cropW = getContext().getResources().getDisplayMetrics().widthPixels;
//        int cropH = getContext().getResources().getDisplayMetrics().heightPixels - 2000;
        //int selector = R.drawable.select_cb;
        FunctionConfig config = new FunctionConfig();
        config.setType(LocalMediaLoader.TYPE_IMAGE);
        config.setCopyMode(FunctionConfig.COPY_MODEL_DEFAULT);
        config.setCompress(false);
        config.setEnablePixelCompress(true);
        config.setEnableQualityCompress(true);
        config.setMaxSelectNum(1);
        config.setSelectMode(FunctionConfig.MODE_SINGLE);
        config.setShowCamera(true);
        config.setEnablePreview(true);
        config.setEnableCrop(true);
        config.setPreviewVideo(false);
        config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
        config.setRecordVideoSecond(60);// 视频秒数
//        config.setCropW(1000);
//        config.setCropH(1000);
        config.setCheckNumMode(false);
        config.setCompressQuality(100);
        config.setImageSpanCount(4);
        config.setSelectMedia(selectMedia);

        config.setThemeStyle(ThemeUtil.getThemeColor(getContext(), R.attr.colorPrimary));
        // 可以自定义底部 预览 完成 文字的颜色和背景色

        // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
        //config.setPreviewColor(ThemeUtil.getThemeColor(getContext(), R.attr.colorPrimary));
        //config.setCompleteColor(R.color.white);
        //config.setPreviewBottomBgColor(R.color.bar_grey);
        //config.setBottomBgColor(R.color.bar_grey);
        //config.setCheckedBoxDrawable(selector);


        // 先初始化参数配置，在启动相册
        PictureConfig.init(config);
        PictureConfig.getPictureConfig().openPhoto(getContext(), resultCallback);
    }


    private ImageView zf_login_yanzhengma, zf_login_yanzhengma_change;
    private EditText et_zf_login_mima, et_zf_login_username, et_zf_login_yanzhengma;
    private Button zf_login_btn, zf_clear_btn;

    private ZfNetData network;
    private Dialog dialogLogin;

    private AlertDialog loginZfDialog(String title) {

        network = new ZfNetData(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        LinearLayout mAlertLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_zf_login, null);
        builder.setView(mAlertLayout);
        //透明
        final AlertDialog dialog = builder.create();

        zf_login_yanzhengma = (ImageView) mAlertLayout.findViewById(R.id.zf_login_yanzhengma);

        zf_login_yanzhengma_change = (ImageView) mAlertLayout.findViewById(R.id.zf_login_yanzhengma_change);
        et_zf_login_mima = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_mima);
        et_zf_login_username = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_username);
        et_zf_login_yanzhengma = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_yanzhengma);
        zf_login_btn = (Button) mAlertLayout.findViewById(R.id.zf_login_btn);
        network.getSecretCode();
        zf_login_yanzhengma_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network.getSecretCode();
            }
        });
        zf_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network.zfLogin(et_zf_login_username.getText().toString(), et_zf_login_mima.getText().toString(), et_zf_login_yanzhengma.getText().toString());
            }
        });
        zf_clear_btn = (Button) mAlertLayout.findViewById(R.id.zf_clear_btn);
        zf_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_zf_login_username.setText("");
                et_zf_login_mima.setText("");
                et_zf_login_yanzhengma.setText("");
                network.getSecretCode();
            }
        });

        return dialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SecretCode event) {
        zf_login_yanzhengma.setImageBitmap(event.getBitmap());
    }

    private PersonInfo personInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PersonInfoEvent event) {
        personInfo = event.getInfo();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CourseEvent event) {
        String personXH = event.getPersonXH();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginResponseCode event) {
        switch (event.getCode()) {
            case LoginResponseCode.LOGIN_OK:
                if (dialogLogin.isShowing()) {
                    dialogLogin.dismiss();
                }

                if (SettingsUtil.getUserCourseTerm() != "") {
                    if (SettingsUtil.getUserCourseType().equals("class")) {
                        getBJCourse(network, personInfo, SettingsUtil.getUserCourseTerm().split(":"));
                    } else {
                        String[] info = SettingsUtil.getUserCourseTerm().split(":");
                        network.getPersonCourseForm(SettingsUtil.getXueHao(), info[0], info[1]);
                    }

                } else {
                    String[] info = MainActivity.getCurrentTerm();
                    SettingsUtil.setUserCourseTerm(info[0] + ":" + info[1]);
                    if ("class".equals(SettingsUtil.getUserCourseType())) {
                        getBJCourse(network, personInfo, info);
                    } else {
                        network.getPersonCourseForm(personInfo.getPersonXH(), info[0], info[1]);
                    }

                }
                break;
            case LoginResponseCode.LOGIN_VERFATION_ERROR:
                Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_ERROR:
                Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_PASSWORD_ERROR:
                Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR:
                Toast.makeText(getContext(), "密码或用户名错误", Toast.LENGTH_SHORT).show();
                break;
            case REALM_COURSE_STO_OK:
                courseDatabase = null;

                courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserCourseTerm(), SettingsUtil.getUserCourseType());

                if (courseDatabase != null) {
                    Log.e("bjcorse", "create view");
                    createView(con, genCellData(courseDatabase, SettingsUtil.getUserCourseType()));
                }
                break;
        }
    }

    private CourseDatabase getCourseFromDatabase(String personXH, String yearAterm, String type) {
        realm = Realm.getDefaultInstance();
        RealmQuery<CourseDatabase> query = realm.where(CourseDatabase.class);
        CourseDatabase courseDatabase = query
                .equalTo("personXH", personXH).equalTo("yearAterm", yearAterm).equalTo("type", type).findFirst();
        return courseDatabase;
    }

    private PersonInfo getPersonInfoFromDatabase(String personXH) {
        realm = Realm.getDefaultInstance();
        RealmQuery<PersonInfo> query = realm.where(PersonInfo.class);
        PersonInfo personInfo = query
                .equalTo("personXH", personXH).findFirst();

        return personInfo;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private List<LocalMedia> selectMedia = new ArrayList<>();

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            if (selectMedia != null) {
                if(selectMedia.get(0).getCutPath()!=null&&selectMedia.get(0).getCutPath()!="") {
                    setBackground(selectMedia.get(0).getCutPath());
                }else {
                    setBackground(selectMedia.get(0).getPath());
                }
               // setBackground(selectMedia.get(0).getPath());
            }
        }
    };

    private void setBackground(String path) {
        Glide.with(getContext()).load(path).skipMemoryCache(true).fitCenter().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                course_layout.setBackground(resource);
            }
        });
        SettingsUtil.setCourseBacGround(path);
    }
}
