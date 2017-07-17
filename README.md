
# RefreshLayout #

[[![](https://www.jitpack.io/v/chencanlin/RefreshLayout.svg)](https://www.jitpack.io/#chencanlin/RefreshLayout)]([![](https://www.jitpack.io/v/chencanlin/RefreshLayout.svg)](https://www.jitpack.io/#chencanlin/RefreshLayout))


**STEP1** Add it in your root build.gradle at the end of repositories:

    	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

**STEP2 Add the dependency**

	   dependencies {
	        compile 'com.github.chencanlin:RefreshLayout:1.0'
	}

**效果预览**

![](http://i.imgur.com/35AGb8v.gif)

**使用**
    
	<com.ccl.perfectisshit.refreshlayout.view.RefreshLayout
        android:id="@+id/refreshlayout_imageview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="gone"
        app:refreshlayout_loading_img_one="@drawable/loading_view_01"
        app:refreshlayout_loading_img_two="@drawable/loading_view_02"
        app:refreshlayout_loading_img_three="@drawable/loading_view_03">

        <com.ccl.perfectisshit.refreshlayout.view.PullableImageView
            android:id="@+id/piv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="fitCenter"
            android:src="@drawable/ssf"
            />
    </com.ccl.perfectisshit.refreshlayout.view.RefreshLayout>
如果设置loading图片必须为三张图片构成的
设置app:refreshlayout_loading_img_one="@drawable/loading_view_01"
app:refreshlayout_loading_img_two="@drawable/loading_view_02"
app:refreshlayout_loading_img_three="@drawable/loading_view_03"
如果需要替换layout

	<com.ccl.perfectisshit.refreshlayout.view.RefreshLayout
        android:id="@+id/refreshlayout_listview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="gone"
        app:refreshlayout_header_layout="@layout/layout_refreshlayout_head">

        <com.ccl.perfectisshit.refreshlayout.view.PullableListView
            android:id="@+id/plv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:divider="@null"
            android:overScrollMode="never"
            android:scrollbars="none"
            />
    </com.ccl.perfectisshit.refreshlayout.view.RefreshLayout>

headerlayout 必须有viewflipper id必须为refreshlayout_header_viewflipper_id 可以直接引用android:id="@id/refreshlayout_header_viewflipper_id"

footerlayout不许包含子类imageview id为refreshlayout_footer_imageview_id 可以直接引用
android:id="@id/refreshlayout_footer_imageview_id"
