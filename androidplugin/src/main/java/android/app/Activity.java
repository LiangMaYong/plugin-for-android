package android.app;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

/**
 * Activity
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class Activity extends ContextThemeWrapper {

    public ActionBar getActionBar() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public Intent getIntent() {
        return null;
    }

    /**
     * Return the application that owns this activity.
     *
     * @return application
     */
    public final Application getApplication() {
        return null;
    }

    public final boolean requestWindowFeature(int featureId) {
        return false;
    }

    static final class NonConfigurationInstances {
    }

    final void attach(Context context, ActivityThread aThread, Instrumentation instr, IBinder token,
                      Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id,
                      NonConfigurationInstances lastNonConfigurationInstances, Configuration config) {
    }

    final void attach(Context context, ActivityThread aThread, Instrumentation instr, IBinder token, int ident,
                      Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id,
                      NonConfigurationInstances lastNonConfigurationInstances, Configuration config) {

    }

    public void setIntent(Intent newIntent) {
    }

    public WindowManager getWindowManager() {
        return null;
    }

    public Window getWindow() {
        return null;
    }

    public LoaderManager getLoaderManager() {
        return null;
    }

    public View getCurrentFocus() {
        return null;
    }

    protected void onCreate(Bundle savedInstanceState) {
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    protected void onPostCreate(Bundle savedInstanceState) {
    }

    protected void onStart() {
    }

    protected void onRestart() {
    }

    public void onStateNotSaved() {
    }

    protected void onResume() {
    }

    protected void onPostResume() {
    }

    public boolean isVoiceInteraction() {
        return false;
    }

    public boolean isVoiceInteractionRoot() {
        return false;
    }

    protected void onNewIntent(Intent intent) {
    }

    protected void onSaveInstanceState(Bundle outState) {

    }

    protected void onPause() {

    }

    protected void onUserLeaveHint() {

    }

    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return false;

    }

    public CharSequence onCreateDescription() {
        return null;

    }

    public void onProvideAssistData(Bundle data) {

    }

    public boolean showAssist(Bundle args) {
        return false;

    }

    protected void onStop() {

    }

    protected void onDestroy() {

    }

    public void reportFullyDrawn() {

    }

    public void onConfigurationChanged(Configuration newConfig) {

    }

    public int getChangingConfigurations() {
        return 0;

    }

    public Object getLastNonConfigurationInstance() {
        return null;

    }

    public Object onRetainNonConfigurationInstance() {
        return null;

    }

    public void onLowMemory() {

    }

    public void onTrimMemory(int level) {

    }

    public FragmentManager getFragmentManager() {
        return null;

    }

    public void onAttachFragment(Fragment fragment) {

    }

    public void startManagingCursor(Cursor c) {

    }

    public void stopManagingCursor(Cursor c) {

    }

    public View findViewById(int id) {
        return null;
    }

    public void setContentView(int layoutResID) {

    }

    public void setContentView(View view) {

    }

    public void setContentView(View view, LayoutParams params) {

    }

    public void addContentView(View view, LayoutParams params) {
    }

    public TransitionManager getContentTransitionManager() {
        return null;
    }

    public void setContentTransitionManager(TransitionManager tm) {
    }

    public Scene getContentScene() {
        return null;
    }

    public void setFinishOnTouchOutside(boolean finish) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onUserInteraction() {
    }

    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    }

    public void onContentChanged() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onAttachedToWindow() {
    }

    @SuppressLint("MissingSuperCall")
    public void onDetachedFromWindow() {
    }

    public boolean hasWindowFocus() {
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return false;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        return false;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public void onPanelClosed(int featureId, Menu menu) {
    }

    public void invalidateOptionsMenu() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public boolean onNavigateUp() {
        return false;
    }

    public boolean onNavigateUpFromChild(Activity child) {
        return false;
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void openOptionsMenu() {
    }

    public void closeOptionsMenu() {
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
    }

    public void unregisterForContextMenu(View view) {
    }

    public void openContextMenu(View view) {
    }

    public void closeContextMenu() {
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
    }

    protected Dialog onCreateDialog(int id) {
        return null;
    }

    protected Dialog onCreateDialog(int id, Bundle args) {
        return null;
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
    }

    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData,
                            boolean globalSearch) {
    }

    public void triggerSearch(String query, Bundle appSearchData) {
    }

    public void takeKeyEvents(boolean get) {
    }

    public LayoutInflater getLayoutInflater() {
        return null;
    }

    public MenuInflater getMenuInflater() {
        return null;
    }

    protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    public void startActivityForResult(Intent intent, int requestCode) {
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
                                           int flagsValues, int extraFlags) throws SendIntentException {
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
                                           int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
    }

    public void startActivity(Intent intent) {
    }

    public void startActivity(Intent intent, Bundle options) {
    }

    public void startActivities(Intent[] intents) {
    }

    public void startActivities(Intent[] intents, Bundle options) {
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues,
                                  int extraFlags) throws SendIntentException {
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues,
                                  int extraFlags, Bundle options) throws SendIntentException {
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        return false;
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode, Bundle options) {
        return false;
    }

    public boolean startNextMatchingActivity(Intent intent) {
        return false;
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle options) {
        return false;
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode, Bundle options) {
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent,
                                           int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent,
                                           int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {

    }

    public Uri getReferrer() {
        return null;

    }

    public Uri onProvideReferrer() {
        return null;

    }

    public String getCallingPackage() {
        return null;

    }

    public ComponentName getCallingActivity() {
        return null;

    }

    public void setVisible(boolean visible) {
    }

    public boolean isFinishing() {
        return false;
    }

    public boolean isDestroyed() {
        return false;
    }

    public boolean isChangingConfigurations() {
        return false;
    }

    public void recreate() {
    }

    public void finish() {

    }

    public void finishAffinity() {

    }

    public void finishFromChild(Activity child) {

    }

    public void finishAfterTransition() {

    }

    public void finishActivity(int requestCode) {

    }

    public void finishActivityFromChild(Activity child, int requestCode) {

    }

    public void finishAndRemoveTask() {

    }

    public boolean releaseInstance() {
        return false;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onActivityReenter(int resultCode, Intent data) {

    }

    public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        return null;

    }

    public void setRequestedOrientation(int requestedOrientation) {

    }

    public int getRequestedOrientation() {
        return 0;
    }

    public int getTaskId() {
        return 0;

    }

    public boolean isTaskRoot() {
        return false;

    }

    public boolean moveTaskToBack(boolean nonRoot) {
        return nonRoot;

    }

    public String getLocalClassName() {
        return null;

    }

    public ComponentName getComponentName() {
        return null;

    }

    public SharedPreferences getPreferences(int mode) {
        return null;
    }

    public Object getSystemService(String name) {
        return name;
    }

    public void setTitle(CharSequence title) {
    }

    public void setTitle(int titleId) {
    }

    public void setTitleColor(int textColor) {
    }

    protected void onTitleChanged(CharSequence title, int color) {
    }

    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return parent;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

    }

    public boolean isImmersive() {
        return false;
    }

    public boolean requestVisibleBehind(boolean visible) {
        return visible;

    }

    public void onVisibleBehindCanceled() {
    }

    public void onEnterAnimationComplete() {
    }

    public void setImmersive(boolean i) {
    }

    public ActionMode startActionMode(Callback callback) {
        return null;
    }

    public ActionMode startActionMode(Callback callback, int type) {
        return null;
    }

    public ActionMode onWindowStartingActionMode(Callback callback) {
        return null;

    }

    public ActionMode onWindowStartingActionMode(Callback callback, int type) {
        return null;

    }

    public void onActionModeStarted(ActionMode mode) {
    }

    public void onActionModeFinished(ActionMode mode) {
    }

    public boolean shouldUpRecreateTask(Intent targetIntent) {
        return false;
    }

    public boolean navigateUpTo(Intent upIntent) {
        return false;
    }

    public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
        return false;
    }

    public Intent getParentActivityIntent() {
        return null;

    }

    public void postponeEnterTransition() {
    }

    public void startPostponedEnterTransition() {
    }

    public void startLockTask() {
    }

    public void stopLockTask() {
    }

    public void showLockTaskEscapeMessage() {
    }

}
