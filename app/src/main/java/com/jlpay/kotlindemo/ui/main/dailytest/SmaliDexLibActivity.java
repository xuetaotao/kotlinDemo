package com.jlpay.kotlindemo.ui.main.dailytest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.util.TypeUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 使用步骤说明：
 * 1.添加依赖库：implementation group: 'org.smali', name: 'dexlib2', version: '2.3.1'
 * 2.添加 SmaliDexLibActivity 类和 activity_smali_dex_lib1 布局文件
 * 3.把 SmaliDexLibActivity 注册到 AndroidManifest
 * 4.调用 newInstance() 方法添加一个进入 SmaliDexLibActivity 的入口
 * 5.把需要分析的apk文件复制到assets目录下
 * 6.对下面注释 "需要初始化" 的方法根据实际情况进行初始化
 */
public class SmaliDexLibActivity extends AppCompatActivity {

    private String TAG = SmaliDexLibActivity.class.getSimpleName();
    private final List<File> oldDexFiles = new ArrayList<>();
    private final Set<String> descOfClassesInApk = new HashSet<>();
    private final Set<Pattern> loaderClassPatterns = new HashSet<>();
    public HashSet<String> mDexLoaderPattern = new HashSet<>();
    private String apkName;
    private String FILE_SAVE_DIR;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, SmaliDexLibActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smali_dex_lib);

        FILE_SAVE_DIR = SmaliDexLibActivity.this.getExternalFilesDir(null) + File.separator + "file" + File.separator;
        Log.e(TAG, "文件保存路径：" + FILE_SAVE_DIR);

        needToInit();

        for (String patternStr : mDexLoaderPattern) {
            loaderClassPatterns.add(Pattern.compile(PatternUtils.dotClassNamePatternToDescriptorRegEx(patternStr)));
        }

        initView();
    }


    /**
     * TODO 需要初始化
     * 1.根据实际情况填写
     * 2.需要把apk文件复制到assets目录下
     */
    private void needToInit() {
        mDexLoaderPattern.add("com.shuabei.mpos.WXApplication");//你的Application包名
        mDexLoaderPattern.add("com.tencent.tinker.loader.*");//固定不变
//        mDexLoaderPattern.add("org.xmlpull.v1.*");//固定不变
        apkName = "app_1.0_1_debug_free_dev.apk";//你的APK文件名
    }

    /**
     * TODO 需要初始化
     */
    public void switchDemoApk(View view) {
        apkName = "app_1.0_1_debug_free_dev.apk";
        Log.e(TAG, "切换到Demo Apk：" + apkName);
        Toast.makeText(this, "切换到Demo Apk", Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO 需要初始化
     */
    public void switchLiApk(View view) {
        apkName = "立刷_3.2.5_2_unkown_debug_lishua_dev.apk";
        Log.e(TAG, "切换到Lishua Apk：" + apkName);
        Toast.makeText(this, "切换到Lishua Apk", Toast.LENGTH_SHORT).show();
    }


    private void initView() {

//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute();

        File file = new File(FILE_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        String[] list = file.list();
        if (list != null && list.length > 0) {
            for (String fileName : list) {
                boolean b = deleteFile(fileName);
                String s = b ? "success" : "fail";
                Log.e(TAG, "delete file:" + fileName + "\t" + s);//TODO 暂时有问题删除不了
            }
        }
    }

    public void smaliDexLibTest(View view) {
        oldDexFiles.clear();
        descOfClassesInApk.clear();
        AssetManager assetManager = getAssets();
        try {
            //创造环境
            Log.e(TAG, "Thread is:" + Thread.currentThread().getName());
            InputStream inputStream = assetManager.open(apkName);
            File oldFile = new File(FILE_SAVE_DIR, apkName);
            copy(inputStream, new FileOutputStream(oldFile));
            collectClassesInDex(oldFile);
            oldDexFiles.add(oldFile);

            if (oldDexFiles.size() == 0) {
                Log.e(TAG, "oldDexFiles.size() is zero");
                return;
            }
            checkIfLoaderClassesReferToNonLoaderClasses();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "出现错误：" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void checkIfLoaderClassesReferToNonLoaderClasses() throws IOException {
        boolean hasInvalidCases = false;
        for (File dexFile : oldDexFiles) {
            Log.e(TAG, "Check if loader classes in " + dexFile.getName()
                    + " refer to any classes that is not in loader class patterns.");
            DexBackedDexFile dex = DexFileFactory.loadDexFile(dexFile, Opcodes.forApi(29));
            for (ClassDef classDef : dex.getClasses()) {
                final String currClassDesc = classDef.getType();
                if (TextUtils.equals(currClassDesc, "Lcom/tencent/tinker/loader/hotplug/IncrementComponentManager;")) {
                    Log.e(TAG, "到了IncrementComponentManager");
                }
                if (!isStringMatchesPatterns(currClassDesc, loaderClassPatterns)) {
                    continue;
                }
                for (Field field : classDef.getFields()) {
                    final String currFieldTypeDesc = field.getType();
                    if (!isReferenceFromLoaderClassValid(currFieldTypeDesc)) {
                        Log.e(TAG, String.format("FATAL: field '%s' in loader class '%s' refers to class '%s' which "
                                        + "is not loader class, this may cause crash when patch is loaded.",
                                field.getName(), currClassDesc, currFieldTypeDesc));
                        hasInvalidCases = true;
                    }
                }
                for (Method method : classDef.getMethods()) {
                    boolean isCurrentMethodInvalid = false;
                    final String currMethodRetTypeDesc = method.getReturnType();
                    if (!isReferenceFromLoaderClassValid(currMethodRetTypeDesc)) {
                        Log.e(TAG, String.format("FATAL: method '%s:%s' in loader class '%s' refers to class '%s' which "
                                        + "is not loader class, this may cause crash when patch is loaded.",
                                method.getName(), getShorty(method.getParameterTypes(), currMethodRetTypeDesc),
                                currClassDesc, currMethodRetTypeDesc));
                        isCurrentMethodInvalid = true;
                    } else {
                        for (CharSequence paramTypeDesc : method.getParameterTypes()) {
                            if (!isReferenceFromLoaderClassValid(paramTypeDesc.toString())) {
                                Log.e(TAG, String.format("FATAL: method '%s:%s' in loader class '%s' refers to class '%s' which "
                                                + "is not loader class, this may cause crash when patch is loaded.",
                                        method.getName(), getShorty(method.getParameterTypes(), currMethodRetTypeDesc),
                                        currClassDesc, paramTypeDesc));
                                isCurrentMethodInvalid = true;
                                break;
                            }
                        }
                    }

                    check_method_impl:
                    {
                        final MethodImplementation methodImpl = method.getImplementation();
                        if (methodImpl == null) {
                            break check_method_impl;
                        }
                        final Iterable<? extends Instruction> insns = methodImpl.getInstructions();
                        if (!insns.iterator().hasNext()) {
                            break check_method_impl;
                        }
                        for (Instruction insn : insns) {
                            if (insn instanceof ReferenceInstruction) {
                                final ReferenceInstruction refInsn = (ReferenceInstruction) insn;
                                switch (refInsn.getReferenceType()) {
                                    case ReferenceType.TYPE: {//TODO 问题就出在这里了
                                        final TypeReference typeRefInsn = (TypeReference) refInsn.getReference();
                                        final String refereeTypeDesc = typeRefInsn.getType();
                                        if (isReferenceFromLoaderClassValid(refereeTypeDesc)) {
                                            break;
                                        }
                                        Log.e(TAG, String.format("FATAL: method '%s:%s' in loader class '%s' refers to class '%s' which "
                                                        + "is not loader class, this may cause crash when patch is loaded.",
                                                method.getName(), getShorty(method.getParameterTypes(), currMethodRetTypeDesc),
                                                currClassDesc, refereeTypeDesc));
                                        isCurrentMethodInvalid = true;
                                        break;
                                    }
                                    case ReferenceType.FIELD: {
                                        final FieldReference fieldRefInsn = (FieldReference) refInsn.getReference();
                                        final String refereeFieldName = fieldRefInsn.getName();
                                        final String refereeFieldDefTypeDesc = fieldRefInsn.getDefiningClass();
                                        if (isReferenceFromLoaderClassValid(refereeFieldDefTypeDesc)) {
                                            break;
                                        }
                                        Log.e(TAG, String.format("FATAL: method '%s:%s' in loader class '%s' refers to field '%s' in class '%s' which "
                                                        + "is not in loader class, this may cause crash when patch is loaded.",
                                                method.getName(), getShorty(method.getParameterTypes(), currMethodRetTypeDesc),
                                                currClassDesc, refereeFieldName, refereeFieldDefTypeDesc));
                                        isCurrentMethodInvalid = true;
                                        break;
                                    }
                                    case ReferenceType.METHOD: {
                                        final MethodReference methodRefInsn = (MethodReference) refInsn.getReference();
                                        final String refereeMethodName = methodRefInsn.getName();
                                        final Collection<? extends CharSequence> refereeMethodParamTypes = methodRefInsn.getParameterTypes();
                                        final String refereeMethodRetType = methodRefInsn.getReturnType();
                                        final String refereeMethodDefClassDesc = methodRefInsn.getDefiningClass();
                                        if (isReferenceFromLoaderClassValid(refereeMethodDefClassDesc)) {
                                            break;
                                        }
                                        Log.e(TAG, String.format("FATAL: method '%s:%s' in loader class '%s' refers to method '%s:%s' in class '%s' which "
                                                        + "is not in loader class, this may cause crash when patch is loaded.",
                                                method.getName(), getShorty(method.getParameterTypes(), currMethodRetTypeDesc),
                                                currClassDesc, refereeMethodName, getShorty(refereeMethodParamTypes, refereeMethodRetType),
                                                refereeMethodDefClassDesc));
                                        isCurrentMethodInvalid = true;
                                        break;
                                    }
                                    default: {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (isCurrentMethodInvalid) {
                        hasInvalidCases = true;
                    }
                }
            }
        }
        if (hasInvalidCases) {
            Log.e(TAG, "There are fatal reasons that cause Tinker interrupt"
                    + " patch generating procedure, see logs above.");
//            throw new IOException("There are fatal reasons that cause Tinker interrupt"
//                    + " patch generating procedure, see logs above.");
        } else {
            Log.e(TAG, "Success!!!!!");
        }
    }

    private boolean isReferenceFromLoaderClassValid(String refereeTypeDesc) {
        if (refereeTypeDesc.equals("Lorg/xmlpull/v1/XmlPullParser;")) {
            Log.e(TAG, "XmlPullParser::" + XmlPullParser.class.getClassLoader().toString() + "\n" +
                    "descOfClassesInApk是否包含XmlPullParser：" + descOfClassesInApk.contains(refereeTypeDesc));
        }
        if (TypeUtils.isPrimitiveType(refereeTypeDesc)) {//是否为原始类型：即int,long之类的
            return true;
        }
        if (!descOfClassesInApk.contains(refereeTypeDesc)) {//该类是否包含在apk中
            return true;
        }
        if (isStringMatchesPatterns(refereeTypeDesc, loaderClassPatterns)) {
            return true;
        }
        Log.e(TAG, "refereeTypeDesc is:" + refereeTypeDesc);
        return false;
    }


    private void collectClassesInDex(File dexFile) throws IOException {
        //收集
        File file = new File(FILE_SAVE_DIR, "descOfClassesInApk-" + apkName + "-" + System.currentTimeMillis() + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        Log.e(TAG, "Collect class descriptors in " + dexFile.getName());
        final DexFile dex = DexFileFactory.loadDexFile(dexFile, Opcodes.forApi(29));
        for (ClassDef classDef : dex.getClasses()) {
            descOfClassesInApk.add(classDef.getType());
            dataOutputStream.writeUTF(classDef.getType() + "\n");
        }
    }

    public static boolean isStringMatchesPatterns(String str, Collection<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            if (pattern.matcher(str).matches()) {
                return true;
            }
        }
        return false;
    }

    public static String getShorty(Collection<? extends CharSequence> params, String returnType) {
        StringBuilder sb = new StringBuilder(params.size() + 1);
        sb.append(getShortyType(returnType));
        for (CharSequence typeRef : params) {
            sb.append(getShortyType(typeRef));
        }
        return sb.toString();
    }

    public static char getShortyType(CharSequence type) {
        if (type.length() > 1) {
            return 'L';
        }
        return type.charAt(0);
    }

    static class PatternUtils {
        public static String dotClassNamePatternToDescriptorRegEx(String dotPattern) {
            if (dotPattern.startsWith("L") && dotPattern.endsWith(";") || dotPattern.startsWith("[")) {
                return dotPattern.replace('.', '/').replace("[", "\\[");
            }

            String descriptor = dotPattern.replace('.', '/');

            StringBuilder sb = new StringBuilder();

            int i;
            for (i = dotPattern.length() - 1; i >= 1; i -= 2) {
                char ch = dotPattern.charAt(i);
                char prevCh = dotPattern.charAt(i - 1);
                if (prevCh == '[' && ch == ']') {
                    sb.append("\\[");
                } else {
                    break;
                }
            }

            descriptor = descriptor.substring(0, i + 1);

            if ("void".equals(descriptor)) {
                descriptor = "V";
                sb.append(descriptor);
            } else if ("boolean".equals(descriptor)) {
                descriptor = "Z";
                sb.append(descriptor);
            } else if ("byte".equals(descriptor)) {
                descriptor = "B";
                sb.append(descriptor);
            } else if ("short".equals(descriptor)) {
                descriptor = "S";
                sb.append(descriptor);
            } else if ("char".equals(descriptor)) {
                descriptor = "C";
                sb.append(descriptor);
            } else if ("int".equals(descriptor)) {
                descriptor = "I";
                sb.append(descriptor);
            } else if ("long".equals(descriptor)) {
                descriptor = "J";
                sb.append(descriptor);
            } else if ("float".equals(descriptor)) {
                descriptor = "F";
                sb.append(descriptor);
            } else if ("double".equals(descriptor)) {
                descriptor = "D";
                sb.append(descriptor);
            } else {
                sb.append('L').append(descriptor);

                if (!descriptor.endsWith(";")) {
                    sb.append(';');
                }
            }

            String regEx = sb.toString();
            regEx = regEx.replace("*", ".*");
            regEx = regEx.replace("?", ".?");
            regEx = regEx.replace("$", "\\$");
            regEx = '^' + regEx + '$';

            return regEx;
        }
    }


    private void copy(InputStream inputStream, OutputStream outputStream) {
        Log.e(TAG, "ThreadCopy is:" + Thread.currentThread().getName());
        if (inputStream == null || outputStream == null) {
            return;
        }
        byte[] buffer = new byte[1024];
        int readLength = 0;
        try {
            while ((readLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
