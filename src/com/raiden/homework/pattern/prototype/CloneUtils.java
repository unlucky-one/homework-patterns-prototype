package com.raiden.homework.pattern.prototype;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/3/14
 */
public class CloneUtils {

   static Object deepClone(Object source) {
        Object cloneObj = null;
        try {
            cloneObj = source.getClass().newInstance();
            Field[] filedArr = source.getClass().getDeclaredFields();
            Field field;//属性
            Class fieldType;//属性类型
            Object filedVal;//属性值
            for (int i = 0; i < filedArr.length; i++) {
                field = filedArr[i];
                fieldType = field.getType();
                field.setAccessible(true);
                filedVal = field.get(source);

                if (Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                if (fieldType.isArray()) {
                    Object cloneArr = Array.newInstance(filedVal.getClass()
                            .getComponentType(), Array.getLength(filedVal));
                    cloneArr(filedVal, cloneArr);
                    filedArr[i].set(cloneObj, cloneArr);
                } else {
                    if (filedVal instanceof Cloneable) {
                        Method cloneMethod;
                        try {
                            cloneMethod = filedVal.getClass().getDeclaredMethod("clone",
                                    new Class[] {});

                        } catch (NoSuchMethodException e) {
                            cloneMethod = filedVal.getClass().getMethod("clone",
                                    new Class[] {});
                        }
                        filedArr[i].set(cloneObj, cloneMethod.invoke(filedVal,
                                new Object[0]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cloneObj;
    }

    static private void cloneArr(Object objArr, Object cloneArr) throws Exception {
        Object objTmp;
        Object val = null;
        for (int i = 0; i < Array.getLength(objArr); i++) {
            //注，如果是非数组的基本类型，则返回的是包装类型
            objTmp = Array.get(objArr, i);
            if (objTmp == null) {
                val = null;
            } else if (objTmp.getClass().isArray()) {//如果是数组

                val = Array.newInstance(objTmp.getClass().getComponentType(), Array
                        .getLength(objTmp));
                cloneArr(objTmp, val);
            } else {
                if (objArr.getClass().getComponentType().isPrimitive()
                        || !(objTmp instanceof Cloneable)) {
                    val = objTmp;
                } else if (objTmp instanceof Cloneable) {
                    Method cloneMethod;
                    try {
                        cloneMethod = objTmp.getClass().getDeclaredMethod("clone",
                                new Class[] {});

                    } catch (NoSuchMethodException e) {
                        cloneMethod = objTmp.getClass()
                                .getMethod("clone", new Class[] {});
                    }
                    cloneMethod.setAccessible(true);
                    val = cloneMethod.invoke(objTmp, new Object[0]);
                }
            }
            Array.set(cloneArr, i, val);
        }
    }
}
