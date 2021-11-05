class Palindrome {
    private String content;
    private int index;
    private int length;

    public void setContent(String content) {
        this.content = content;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }

    public Palindrome() {}
    public Palindrome(String str, int ind, int len) {
        content = str;
        index = ind;
        length = len;
    }

    @Override
    public String toString() {
        return "Palindrome{" +
                "content='" + content + '\'' +
                ", index=" + index +
                ", length=" + length +
                '}';
    }
}