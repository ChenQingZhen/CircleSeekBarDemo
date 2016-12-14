# CircleSeekBarDemo
圆形SeekBar，相当于仪表盘

Simple to use:
this is default style
```xml
 <com.cqz.mylibrary.CircleSeekBar
        android:id="@+id/circle_seek_bar"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:background="@android:color/holo_green_light"
        />
```

![image](https://github.com/ChenQingZhen/CircleSeekBarDemo/blob/master/Screenshot/Screenshot_1481703757.png)
<br/>
And you can custom style
like this:
```xml
 <com.cqz.mylibrary.CircleSeekBar
        android:id="@+id/circle_seek_bar"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:arc_color="@color/colorAccent"
        app:arc_width="32dp"
        app:max_progress="100"
        app:current_progress="0"
        app:start_angle="135"
        app:max_angle="270"
        app:point_color="@color/colorPrimary"
        app:point_radius="24dp"
        android:background="@android:color/holo_green_light"
        />
    ```
    
![image](https://github.com/ChenQingZhen/CircleSeekBarDemo/blob/master/Screenshot/Screenshot_custom.png)
  
