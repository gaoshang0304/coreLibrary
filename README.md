# coreLibrary

## 主要内容
# 1. xtablayout : easy to custom the indecator's length and the other attributes ex indecator's shape;
# 2. photo picker and editor : similar wx photo picker and editor;
# use func:
OnPhotoPickListener mListener = new OnPhotoPickListener();
PhotoPicker.init().setMaxCount(9).setShowCamera(false).setUseSystemCamera(false)
                    .setPreviewEnable(true).startPick(mContext, mListener);
                    
# 3. BaseActivity , BaseMvpActivity , BaseFragment , BaseMvpFragment , let you fast start a new App.
