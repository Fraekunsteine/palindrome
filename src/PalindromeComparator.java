import java.util.Comparator;

public class PalindromeComparator implements Comparator<Palindrome> {
    @Override
    public int compare(Palindrome p1, Palindrome p2) {
        if(p1.getLength() < p2.getLength()) return 1;
        else if(p1.getLength() > p2.getLength()) return -1;
        else {
            if(p1.getIndex() < p2.getIndex()) return 1;
            else if(p1.getIndex() > p2.getIndex()) return -1;
            else return 0;
        }
    }
}
