import java.util.*;

public class PalindromeTester {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        PalindromeTester pt = new PalindromeTester();
        String input = sc.nextLine();
        List<Palindrome> palindromes = pt.findPalindromes(input);

        Collections.sort(palindromes, new PalindromeComparator());
        for(Palindrome p : palindromes) System.out.printf("%s,%d,%d\n", p.getContent(), p.getIndex(), p.getLength());

        sc.close();
    }
    private String reverse(String s) {
        String reverse = "";
        for(int i = s.length()-1; i>=0; i--) {
            reverse += s.charAt(i);
        }
        return reverse;
    }
    private List<Palindrome> findPalindromes(String str) {
        List<Palindrome> palindromes = new ArrayList<Palindrome>();
        Stack<Letter> lookup = new Stack<Letter>();
        Palindrome palindrome = null;
        for (int i = 0; i < str.length(); i++) {
            Letter l = new Letter(str.charAt(i), i);
            if (!lookup.isEmpty()) {
                Letter temp = lookup.pop();
                Letter target = null;
                if (!lookup.isEmpty()) target = lookup.peek();
                if (target != null && target.getCharacter() == l.getCharacter()) {
                    int length = l.getIndex() - target.getIndex() + 1;
                    if (palindrome != null && !(palindrome.getIndex() >= target.getIndex() && palindrome.getIndex() + palindrome.getLength() <= target.getIndex() + length))
                        palindromes.add(palindrome);
                    palindrome = new Palindrome(str.substring(target.getIndex(), target.getIndex() + length), target.getIndex(), length);
                    lookup.pop();
                } else if (temp.getCharacter() == l.getCharacter()) {
                    int length = l.getIndex() - temp.getIndex() + 1;
                    if (palindrome != null && !(palindrome.getIndex() >= temp.getIndex() && palindrome.getIndex() + palindrome.getLength() <= temp.getIndex() + length))
                        palindromes.add(palindrome);
                    palindrome = new Palindrome(str.substring(temp.getIndex(), temp.getIndex() + length), temp.getIndex(), length);
                    lookup.push(temp);
                } else {
                    lookup.push(temp);
                }
            }
            if (lookup.isEmpty() && palindrome != null) {
                palindromes.add(palindrome);
                palindrome = null;
            }
            else lookup.push(l);
        }
        if(palindrome != null) palindromes.add(palindrome);
        return palindromes;
    }
}
