package com.voyageone.tools.dao.codegen;


import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by Ethan Shi on 2016/5/6.
 */
public class VOCmsDaoGeneratorTest {

    public static void main(String[] arg)
    {
        Integer i1 = 1;
        Integer i2 = 1;

        System.out.println( i1 == i2);

        Integer i3 = new Integer(1) ;
        Integer i4 = new Integer(1);

        System.out.println( i3 == i4); //

        System.out.println( i3.equals(i4));

        BaseModel baseModel =  new BaseModel();
        BaseModel baseModel2 =  new BaseModel();

        baseModel.setId(new Integer(1));
        baseModel2.setId(new Integer(1));

        System.out.println( i1 == baseModel.getId());
        System.out.println( baseModel2.getId() == baseModel.getId());

        String str = "test";

        System.out.println( str +  baseModel.getId());

        BaseModel baseModel3 =  new BaseModel();
        System.out.println( baseModel3.getId() == null || baseModel3.getId() == 0);

    }

}