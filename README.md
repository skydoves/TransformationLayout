

<h1 align="center">TransformationLayout</h1></br>
<p align="center"> 
🌠 Transform into a different view or activity using morphing animations.
<br>Using<a href="https://material.io/design/motion/the-motion-system.html" target="_blank"> Transformation motions </a> of new material version.
</p>
</br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/DisneyMotions/actions"><img alt="Build Status" src="https://github.com/skydoves/TransformationLayout/workflows/Android%20CI/badge.svg"/></a> 
  <a href="https://github.com/skydoves"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=skydoves&color=C51162"/></a> 
</p>

## Screeshots
<p align="center">
<img src="/preview/preview0.gif" width="32%"/>
<img src="/preview/preview1.gif" width="32%"/>
<img src="/preview/preview2.gif" width="32%"/>
</p>

## Including in your project
[![Download](https://api.bintray.com/packages/devmagician/maven/transformationlayout/images/download.svg) ](https://bintray.com/devmagician/maven/transformationlayout/_latestVersion)
[![JitPack](https://jitpack.io/v/skydoves/TransformationLayout.svg)](https://jitpack.io/#skydoves/TransformationLayout)
### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
        jcenter()
    }
}
```
And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "com.github.skydoves:transformationlayout:1.0.1"
}
```

## Usage
Add following XML namespace inside your XML layout file.

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### TransformationLayout
Here is a basic example of implementing `TransformationLayout`. <br>
We must wrap one or more views that we want to transform.

```gradle
<com.skydoves.transformationlayout.TransformationLayout
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  app:transformation_targetView="@+id/my_cardView" // sets a target view.
  app:transformation_duration="450" // sets a duration of the transformation.
  app:transformation_direction="auto" // auto, entering, returning
  app:transformation_fadeMode="in" // in, out, cross, through
  app:transformation_fitMode="auto" // auto, height, width
  app:transformation_pathMode="arc" // arc, linear
>

   <!-- other views -->

</com.skydoves.transformationlayout.TransformationLayout>
```

### Transform into a view
Here is an simple example to transform view A to B.

<img src="https://user-images.githubusercontent.com/24237865/75549488-25321700-5a73-11ea-8908-609592907e84.gif" align="right" width="32%"/>

```gradle
<com.skydoves.transformationlayout.TransformationLayout
    android:id="@+id/transformationLayout"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:transformation_duration="550"
    app:transformation_targetView="@+id/myCardView">

  <com.google.android.material.floatingactionbutton.FloatingActionButton
      android:id="@+id/fab"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:backgroundTint="@color/colorPrimary"
      android:src="@drawable/ic_write"/>
</com.skydoves.transformationlayout.TransformationLayout>

<com.google.android.material.card.MaterialCardView
    android:id="@+id/myCardView"
    android:layout_width="240dp"
    android:layout_height="312dp"
    android:layout_marginLeft="30dp"
    android:layout_marginTop="30dp"
    app:cardBackgroundColor="@color/colorPrimary" />
```


#### Bind a TargetView
We can bind a targetView that will be transformed from the `TransformationLayout` using the below attribute in XML.<br>
If you bind a targetView to the `TransformationLayout`, the targetView's visibility will be `GONE`.

```gradle
app:transformation_targetView="@+id/myCardView"
```
Or we can bind a targetView using method.
```kotlin
transformationLayout.bindTargetView(myCardView)
```

#### Starting and finishing the transformation
After binding a targetView, we can start or finish transformation using the below methods.<br>
The `startTransform` and `finishTransform` methods need a `parent` as a parameter.<br>
The parent parameter should be the root layout (the highest level layout).
```kotlin
// start transformation when touching the fab.
fab.setOnClickListener {
  transformationLayout.startTransform(parent)
}

// finish transformation when touching the myCardView.
myCardView.setOnClickListener {
  transformationLayout.finishTransform(parent)
}
```

### Transform into an Activity

<img src="/preview/preview2.gif" align="right" width="32%"/>

Here is an example of a transforming floating action button to Activity. <br>
We don't need to bind a targetView.

```gradle
<com.skydoves.transformationlayout.TransformationLayout
    android:id="@+id/transformationLayout"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:transformation_duration="550">

  <com.google.android.material.floatingactionbutton.FloatingActionButton
      android:id="@+id/fab"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:backgroundTint="@color/colorPrimary"
      android:src="@drawable/ic_write"/>
</com.skydoves.transformationlayout.TransformationLayout>
```
#### onTransformationStartContainer
We should add `onTransformationStartContainer()` to the Activity that has the floating action button. If your view is in the fragment, the code should be added to the fragment's Activity. It must be called before `super.onCreate`.
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationStartContainer() // should be called before super.onCreate().
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
}
```
Here is the Java way.
```java
TransitionExtensionKt.onTransformationStartContainer(this);
```

#### startActivity
And we should call `startActivity` with bundle and intent data.<br>
We should get a `bundle` using `withActivity` method. It needs a context and any name of transition. <br>
The `bundle` must be used as `startActivity`'s parameter.<br>
We should put parcelable data to the intent using `getParcelableParams()` method.<br>
The extra name of the parcelable data can be anything, and it will be reused later.
```kotlin
fab.setOnClickListener {
    val bundle = transformationLayout.withActivity(this, "myTransitionName")
    val intent = Intent(this, DetailActivity::class.java)
    intent.putExtra("TransformationParams", transformationLayout.getParcelableParams())
    startActivity(intent, bundle)
}
```
If we want to get bundle data in RecyclerView or other classes, <br>
we can use `withView` and `withContext` instead of `withActivty`.
```kotlin
// usage in the RecyclerView.Adapter
override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
   val bundle = transformationLayout.withView(holder.itemView, "myTransitionName")
}
```

Here is the Java way.
```java
Bundle bundle = transformationLayout.withActivity(this, "myTransitionName");
Intent intent = new Intent(this, DetailActivity.class);
intent.putExtra("TransformationParams", transformationLayout.getParcelableParams());
startActivity(intent, bundle);
```

#### onTransformationEndContainer
And finally, we should add `onTransformationEndContainer()` to the Activity that will be started. <br>
It must be added before `super.onCreate`.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationEndContainer(intent.getParcelableExtra("TransformationParams"))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
}
```

Here is the Java way.
```java
TransformationLayout.Params params = getIntent().getParcelableExtra("myTransitionName");
TransitionExtensionKt.onTransformationEndContainer(this, params);
```

### OnTransformFinishListener
We can listen a `TransformationLayout` is transformed or not using `OnTransformFinishListener`. <br>
```kotlin
transformationLayout.setOnTransformFinishListener {
  Toast.makeText(context, "is transformed: $it", Toast.LENGTH_SHORT).show()
}
```
Here is the Java way.
```java
transformationLayout.onTransformFinishListener = new OnTransformFinishListener() {
  @Override public void onFinish(boolean isTransformed) {
    Toast.makeText(context, "is transformed:" + isTransformed, Toast.LENGTH_SHORT).show();
  }
};
```

## TransformationLayout Attributes
Attributes | Type | Default | Description
--- | --- | --- | ---
targetView | resource id | none | Bind a targetView that will be transformed. 
duration | Long | 350L | Duration of the transformation.
pathMotion | Motion.ARC, Motion.LINEAR | default layout | Indicates that this transition should be drawn as the which path.
containerColor | Color | Color.TRANSPARENT | Set the container color to be used as the background of the morphing container.
scrimColor | Color | Color.TRANSPARENT | Set the color to be drawn under the morphing container.
direction | Direction.AUTO, Direction.ENTER, Direction.RETURN | Direction.AUTO | Set the direction to be used by this transform.
fadeMode | FadeMode.IN, FadeMode.OUT, FadeMode.CROSS, FadeMode.THROUGH | FadeMode.IN | Set the FadeMode to be used to swap the content of the start View with that of the end View.
fitMode | FitMode.AUTO, FitMode.WIDTH, FitMode.HEIGHT | FitMode.AUTO | Set the fitMode to be used when scaling the incoming content of the end View.


## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/transformationlayout/stargazers)__ for this repository. :star:

# License
```xml
Copyright 2020 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
