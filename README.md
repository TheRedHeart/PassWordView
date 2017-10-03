# PassWordViewDemo     **[简书博客](http://www.jianshu.com/p/da7c88f75ebb)**

![](http://upload-images.jianshu.io/upload_images/2089169-662ba2bb6fd60a03.gif?imageMogr2/auto-orient/strip)
![](http://upload-images.jianshu.io/upload_images/2089169-5b2015e009a2a1c1.gif?imageMogr2/auto-orient/strip)

    <com.lxj.passView.view.PassWordLayout
        android:id="@+id/passLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/tv_tipsinfo"
        android:layout_marginTop="44px"
        app:box_draw_type="rect"
        app:interval_width="4"
        app:item_height="35"
        app:item_width="35"
        app:pass_inputed_type="stars"
        app:pass_leng="six" />

    <declare-styleable name="PassWordLayoutStyle">
        <attr name="box_input_color" format="reference"></attr>//输入框输入状态颜色
        <attr name="box_no_input_color" format="reference"></attr>//输入框未输入状态颜色
        <attr name="input_line_color" format="reference"></attr>//输入线颜色
        <attr name="text_input_color" format="reference"></attr>//文本颜色
        <attr name="interval_width" format="integer"></attr>//间隔
        <attr name="item_width" format="integer"></attr>//子View宽
        <attr name="item_height" format="integer"></attr>//子View高
        <attr name="draw_txt_size" format="integer"></attr>//文本大小
        <attr name="draw_box_line_size" format="integer"></attr>//输入线条大小
        <attr name="is_show_input_line" format="boolean"></attr>//是否显示输入线
        <attr name="pass_inputed_type">//密码输入显示内容
            <flag name="stars" value="0" />
            <flag name="circle" value="1" />
            <flag name="text" value="2" />
        </attr>
        <attr name="box_draw_type">//密码框形状
            <flag name="rect" value="0" />
            <flag name="circle" value="1" />
            <flag name="line" value="2" />
        </attr>
        <attr name="pass_leng">//密码长度 只考虑4 6 8
            <flag name="four" value="4" />
            <flag name="six" value="6" />
            <flag name="eight" value="8" />
        </attr>
    </declare-styleable>