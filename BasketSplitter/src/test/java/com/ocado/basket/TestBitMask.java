package com.ocado.basket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestBitMask {
    @Test
    void initBitMask(){
        BitMask bitMask = new BitMask(10);
        for(boolean bit: bitMask.getNumber()){
            Assertions.assertFalse(bit);
        }
    }
    @Test
    void incrementBitMask(){
        int length = 10;
        BitMask bitMask = new BitMask(length);
        for(int i = 1;i < Math.pow(2, length); i++){
            bitMask.increment();
            int binaryToDecimal = 0;
            for(int j = length - 1;j >= 0; j--){
                if(bitMask.getNumber()[j]){
                    binaryToDecimal += Math.pow(2, Math.abs(j - (length - 1)));
                }
            }
            Assertions.assertEquals(i, binaryToDecimal);
        }
    }
    @Test
    void sumBinaryMask(){
        BitMask bitMask = new BitMask(3);
        bitMask.increment();
        Assertions.assertEquals(1, bitMask.sumBinaryNumber());
        bitMask.increment();
        bitMask.increment();
        Assertions.assertEquals(2, bitMask.sumBinaryNumber());
    }
}
