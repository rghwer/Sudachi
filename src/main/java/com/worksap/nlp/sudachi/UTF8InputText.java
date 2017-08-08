package com.worksap.nlp.sudachi;

import java.util.List;

import com.worksap.nlp.sudachi.dictionary.Grammar;

class UTF8InputText implements InputText<byte[]> {
    
    private final String originalText;
    private final String modifiedText;
    private final byte[] bytes;
    private final List<Integer> offsets;
    private final List<Integer> byteIndexes;
    private final List<List<String>> charCategories;
    private final List<Integer> charCategoryContinuities;
    
    UTF8InputText(Grammar grammar, String originalText, String modifiedText,
        byte[] bytes, List<Integer> offsets, List<Integer> byteIndexes,
        List<List<String>> charCategories, List<Integer> charCategoryContinuities) {
        
        this.originalText = originalText;
        this.modifiedText = modifiedText;
        this.bytes = bytes;
        this.offsets = offsets;
        this.byteIndexes = byteIndexes;
        this.charCategories = charCategories;
        this.charCategoryContinuities = charCategoryContinuities;
    }
    
    @Override
    public String getOriginalText() {
        return originalText;
    }
    
    @Override
    public String getText() {
        return modifiedText;
    }
    
    byte[] getByteText() {
        return bytes;
    }
    
    @Override
    public String getSubstring(int begin, int end)
        throws StringIndexOutOfBoundsException {
        return modifiedText.substring(byteIndexes.get(begin), byteIndexes.get(end));
    }
    
    int getOffsetTextLength(int offset)
        throws IndexOutOfBoundsException {
        return byteIndexes.get(offset);
    }
    
    public boolean isCharAlignment(int offset) {
        return (bytes[offset] & 0xC0) != 0x80;
    }
    
    @Override
    public int getOriginalOffset(int offset)
        throws IndexOutOfBoundsException {
        return offsets.get(offset);
    }
    
    @Override
    public List<String> getCharCategoryNameList(int offset)
        throws IndexOutOfBoundsException {
        return charCategories.get(byteIndexes.get(offset));
    }
    
    @Override
    public int getCharCategoryContinuousLength(int offset)
        throws IndexOutOfBoundsException {
        return charCategoryContinuities.get(offset);
    }
    
    @Override
    public int getCodePointsOffsetLength(int offset, int codePointLength)
        throws IndexOutOfBoundsException {
        int length = 0;
        int target = byteIndexes.get(offset) + codePointLength;
        for (int i = offset; i < bytes.length; i++) {
            if (byteIndexes.get(i) >= target) {
                return length;
            }
            length++;
        }
        return length;
    }
    
}
