package com.github.wangchenning.leaf.util.string;


public class StringUtil {
	public static boolean nullOrEmpty(String str) {
		return null == str || str.isEmpty();
	}
	
	public static  void main(String[] args) {
		assert !nullOrEmpty(""):"123";
		assert nullOrEmpty(null);
		assert !nullOrEmpty("hi");

	}
	
//    public static void main(String[] args) {
//        
////        test1(-5);
//        test2(-3);
//        test1(3);
//        test2(5);
//    }
    
    private static void test1(int a){
        assert a > 0;
        System.out.println(a);
    }
    private static void test2(int a){
        assert a > 0 : "something goes wrong here, a cannot be less than 0";
        System.out.println(a);
    }
}
