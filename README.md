<h1 align="center">TransformationLayout</h1></br>
<p align="center"> 
🌠 Transform views, activity, and fragments into other components with container transform animations.
</p>
</br>

<p align="center">
  <a href="https://devlibrary.withgoogle.com/products/android/repos/skydoves-TransformationLayout"><img alt="Google" src="https://skydoves.github.io/badges/google-devlib.svg"/></a>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/TransformationLayout/actions"><img alt="Build Status" src="https://github.com/skydoves/TransformationLayout/workflows/Android%20CI/badge.svg"/></a> 
  <a href="https://github.com/skydoves"><img alt="Profile" src="https://skydoves.github.io/badges/skydoves.svg"/></a>
</p>

## Download
Go to the [Releases](https://github.com/skydoves/TransformationLayout/releases) to download the demo APK.

## Screenshots
<p align="center">
<img src="/preview/preview0.gif" width="270"/>
<img src="/preview/preview1.gif" width="270"/>
<img src="/preview/preview2.gif" width="270"/>
</p>

## Morphing Animation for Jetpack Compose
If you want to implement morphing animation in Jetpack Compose, check out [Orbital](https://github.com/skydoves/Orbital).

## Including in your project
[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/transformationlayout.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22transformationlayout%22)

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("com.github.skydoves:transformationlayout:1.1.5")
}
```

## Usage
Add the XML namespace below inside your XML layout file:

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### TransformationLayout
`TransformationLayout` is an essential concept to transform your Views, Activities, and Fragments into other components. You must wrap one or more Views that are supposed to be transformed using `TransformationLayout` like the example code below:

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

   <!-- other complicated views -->

</com.skydoves.transformationlayout.TransformationLayout>
```

### Transform into a view
For instance, you can transform a floating button into a CardView as you've seen in the example below:

<img src="https://user-images.githubusercontent.com/24237865/75549488-25321700-5a73-11ea-8908-609592907e84.gif" align="right" width="280"/>

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
With the attribute below in your XML file, you can bind a `targetView` that should be transformed from the `TransformationLayout`. If you bind a targetView with a `TransformationLayout`, the targetView's visibility will be `GONE` by default.

```gradle
app:transformation_targetView="@+id/myCardView"
```

You can also bind a targetView with a `TransformationLayout` using `bindTargetView` method like the code below:

```kotlin
transformationLayout.bindTargetView(myCardView)
```

#### Starting and finishing the transformation
After binding a targetView, we can start or finish transformation using the below methods.<br>
```kotlin
// start transformation when touching the fab.
fab.setOnClickListener {
  transformationLayout.startTransform()
}

// finish transformation when touching the myCardView.
myCardView.setOnClickListener {
  transformationLayout.finishTransform()
}
```
Here are other functionalities to starting and finishing transformation.

```kotlin
// starts and finishes transformation 1000 milliseconds later.
// If we use this method on onCreate() method, it will starts transformation automatically 200ms later.
transformationLayout.startTransformWithDelay(200)
transformationLayout.finishTransformWithDelay(200)

// starts and finishes transformation with stopping a parent layout.
transformationLayout.startTransform(parent)
transformationLayout.finishTransform(parent)
```

### OnTransformFinishListener
We can listen a `TransformationLayout` is transformed or not using `OnTransformFinishListener`. <br>
```kotlin
transformationLayout.setOnTransformFinishListener {
  Toast.makeText(context, "is transformed: $it", Toast.LENGTH_SHORT).show()
}
```
Here is the __Java__ way.
```java
transformationLayout.onTransformFinishListener = new OnTransformFinishListener() {
  @Override public void onFinish(boolean isTransformed) {
    Toast.makeText(context, "is transformed:" + isTransformed, Toast.LENGTH_SHORT).show();
  }
};
```

### Transform into an Activity
We can implement transformation between activities easily using `TransformationActivity` and `TransformationCompat`.

<img src="/preview/preview2.gif" align="right" width="270"/>

Here is an example of transforming a floating action button to Activity. <br>
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
TransformationCompat.onTransformationStartContainer(this);
```

#### TransformationAppCompatActivity
Extends `TransformationAppCompatActivity` or `TransformationActivity` to your activity that will be transformed.
```kotlin
class DetailActivity : TransformationAppCompatActivity()
```
Here is the Java way.
```java
public class DetailActivity extends TransformationAppCompatActivity 
```

#### TransformationCompat
And start the `DetailActivity` using the `TransformationCompat.startActivity` method.
```kotlin
val intent = Intent(context, DetailActivity::class.java)
TransformationCompat.startActivity(transformationLayout, intent)
```
Here is the Java way.
```java
Intent intent = new Intent(context, DetailActivity.class);
TransformationCompat.startActivity(transformationLayout, intent);
```

### Manually Transform into an Activity

<img src="/preview/preview2.gif" align="right" width="270"/>

Here is an example of transforming a floating action button to Activity. <br>
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
TransformationCompat.onTransformationStartContainer(this);
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

Here is the __Java__ way.
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

Here is the __Java__ way.
```java
TransformationLayout.Params params = getIntent().getParcelableExtra("TransformationParams");
TransformationCompat.onTransformationEndContainer(this, params);
```

### Transform into a Fragment
We can implement transformation between fragments for a single Activity application.<br>
Here is an example of transforming a floating action button in Fragment A to Fragment B.

<img src="https://user-images.githubusercontent.com/24237865/80108763-a1e6fa80-85b7-11ea-9350-f9d8ebc46310.gif" align="right" width="270"/>

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
We should call `onTransformationStartContainer()` in the Fragment A that has the floating action button.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  onTransformationStartContainer()
}
```

Here is the Java way.
```java
TransformationCompat.onTransformationStartContainer(this);
```

#### getBundle and addTransformation
We should get a bundle from the `TransformationLayout` and put it into the argument.<br>
And in the fragment manager's transaction, we should add the `TransformationLayout` using `addTransformation` method.

```kotlin
val fragment = MainSingleDetailFragment()
val bundle = transformationLayout.getBundle("TransformationParams")
bundle.putParcelable(MainSingleDetailFragment.posterKey, poster)
fragment.arguments = bundle

requireFragmentManager()
  .beginTransaction()
  .addTransformation(transformationLayout)
  .replace(R.id.main_container, fragment, MainSingleDetailFragment.TAG)
  .addToBackStack(MainSingleDetailFragment.TAG)
  .commit()
}
```
Here is the Java way
```java
MainSingleDetailFragment fragment = new MainSingleDetailFragment();
Bundle bundle = transformationLayout.getBundle("TransformationParams", "transitionName");
fragment.setArguments(bundle);

FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
TransformationCompat.addTransformation(
    fragmentTransaction, transformationLayout, "transitionName");
fragmentTransaction.replace(R.id.main_container, fragment, MainSingleDetailFragment.TAG)
    .addToBackStack(MainSingleDetailFragment.TAG)
    .commit();
```
#### Transition name in Fragment A
We must set a specific transition name to the `TransformationLayout`.<br>
If you want to transform a recyclerView's item, set transiton name in `onBindViewHolder`.
```kotlin
transformationLayout.transitionName = "myTransitionName"
```
Here is the Java way.
```java
transformationLayout.setTransitionName("myTransitionName");
```
#### onTransformationEndContainer in Fragment B
We should get a `TransformationLayout.Params` from the arguments, and call `onTransformationEndContainer` method.<br>
It must be called in `onCreate` method.
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)

  val params = arguments?.getParcelable<TransformationLayout.Params>("TransformationParams")
  onTransformationEndContainer(params)
}
```
Here is the Java way.
```java
TransformationLayout.Params params = getArguments().getParcelable("TransformationParams");
TransformationCompat.onTransformationEndContainer(this, params);
```
#### Transition name in Fragment B
And finally set the specific transition name (same as the transformationLayot in Fragment A) <br>
to the target view in Fragment B in `onViewCreated`.
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
   
  detail_container.transitionName = "myTransitionName"
}
```


## TransformationLayout Attributes
Attributes | Type | Default | Description
--- | --- | --- | ---
targetView | resource id | none | Bind a targetView that will be transformed. 
duration | Long | 350L | Duration of the transformation.
pathMotion | Motion.ARC, Motion.LINEAR | default layout | Indicates that this transition should be drawn as the which path.
containerColor | Color | Color.TRANSPARENT | Set the container color to be used as the background of the morphing container.
allContainerColor | Color | Color.TRANSPARENT | The all container colors (start and end) to be used as the background of the morphing container.
scrimColor | Color | Color.TRANSPARENT | Set the color to be drawn under the morphing container.
direction | Direction.AUTO, Direction.ENTER, Direction.RETURN | Direction.AUTO | Set the direction to be used by this transform.
fadeMode | FadeMode.IN, FadeMode.OUT, FadeMode.CROSS, FadeMode.THROUGH | FadeMode.IN | Set the FadeMode to be used to swap the content of the start View with that of the end View.
fitMode | FitMode.AUTO, FitMode.WIDTH, FitMode.HEIGHT | FitMode.AUTO | Set the fitMode to be used when scaling the incoming content of the end View.
startElevation | Float | ELEVATION_NOT_SET | The elevation that will be used to render a shadow around the container at the start of the transition.
endElevation | Float | ELEVATION_NOT_SET | The elevation that will be used to render a shadow around the container at the end of the transition.
elevationShadowEnabled | Boolean | true if (version > Pie) | Whether shadows should be drawn around the container to approximate native elevation shadows on the start and end views.
holdAtEndEnabled | Boolean | false | Whether to hold the last frame at the end of the animation.

## Additional 🎈
You can reference the usage of the TransformationLayout in another repository [MarvelHeroes](https://github.com/skydoves/MarvelHeroes). <br>
A demo application based on modern Android application tech-stacks and MVVM architecture.

![screenshot](https://user-images.githubusercontent.com/24237865/80602029-9426ee80-8a69-11ea-866d-4e31b6526ab2.png)

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/transformationlayout/stargazers)__ for this repository. :star: <br>
And __[follow](https://github.com/skydoves)__ me for my next creations! 🤩

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
