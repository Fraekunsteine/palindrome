import java.util.*;

public class PalindromeTester {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        //Generate list of palindromes from string
        List<Palindrome> palindromes = new ArrayList<Palindrome>();
        new PalindromeTester().findPalindromes(input, palindromes);

        //Sort palindromes with custom comparator
        Collections.sort(palindromes, new PalindromeComparator());
        int[] duplicates = new int[input.length() + 1];
        int id = 1;
        for (Palindrome p : palindromes) {
            //Check for subsets of palindromes as duplicates
            if (duplicates[p.getIndex()] == 0 || duplicates[p.getIndex() + p.getLength() - 1] == 0) {
                printPalindrome(p, duplicates, id);
            }
            else {
                //obtain potential overlaps with different palindromes and compare
                Palindrome overlapLeft = null;
                if (duplicates[p.getIndex()] > 0) overlapLeft = palindromes.get(duplicates[p.getIndex()] - 1);
                Palindrome overlapRight = null;
                if (duplicates[p.getIndex() + p.getLength() - 1] > 0) overlapRight = palindromes.get(duplicates[p.getIndex() + p.getLength() - 1] - 1);
                if (overlapLeft != null && overlapRight != null &&
                        p.getIndex() < overlapRight.getIndex() &&
                        p.getIndex() + p.getLength() > overlapLeft.getIndex() + overlapLeft.getLength()) {
                    printPalindrome(p, duplicates, id);
                }
            }
            id++;
        }
        sc.close();
    }
    private static void printPalindrome(Palindrome p, int[] duplicates, int id) {
        System.out.printf("%s,%d,%d\n", p.getContent(), p.getIndex(), p.getLength());
        for(int i = p.getIndex(); i < p.getIndex() + p.getLength(); i++) {
            duplicates[i] = id;
        }
    }
    //for debug purposes
    static String visualString(String s, int index, int repeatInd, int targetInd) {
        String ret = "";
        for(int i = 0; i < s.length(); i++) {
            if(i == targetInd) ret += "(";
            if(i == repeatInd) ret +="{";
            if(i == index) ret += "[";
            ret += s.charAt(i);
            if(i != index && i != repeatInd && i != targetInd) ret += " ";
            if(i == index) ret += "] ";
            if(i == repeatInd) ret +="} ";
            if(i == targetInd) ret += ") ";
        }
        return ret;
    }
    //helper function to detect palindromes by expanding lower and upper bounds
    private void findPalindromes(String str, List<Palindrome> palindromes, int lower, int upper) {
        Palindrome palindrome = null;
        for(int i = lower, j = upper; i >= 0 && j < str.length(); i--, j++) {
            if(str.charAt(i) != str.charAt(j)) break;
            int length = j - i + 1;
            //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
            if (palindrome != null && !(palindrome.getIndex() >= i && palindrome.getIndex() + palindrome.getLength() <= j)) {
                palindromes.add(palindrome);
            }
            palindrome = new Palindrome(str.substring(i, j + 1), i, length);
        }
        if(palindrome != null) palindromes.add(palindrome);
    }
    private void findPalindromes(String str, List<Palindrome> palindromes) {
        Palindrome palindrome = null;
        int length = 0, targetInd = -1, repeatInd = 0;
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            char temp = str.charAt(repeatInd), target = ' ';
            if (targetInd > -1) target = str.charAt(targetInd);

            //two checks will be done (in order of potential yield in terms of palindrome length):
            // 1) index (targetInd) of the trailing letter to look for on opposite end of palindrome
            // 2) index (repeatInd) directly preceding current index (repeating letters)

            if (target == c) { //check for targetInd
                findPalindromes(str, palindromes, targetInd, i);
                repeatInd = i; //update repeatInd to current index
            } else if (temp == c) { //check for repeatInd
                length = i - repeatInd + 1;
                //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
                if (palindrome != null && !(palindrome.getIndex() >= repeatInd && palindrome.getIndex() + palindrome.getLength() <= repeatInd + length)) {
                    palindromes.add(palindrome);
                }
                palindrome = new Palindrome(str.substring(repeatInd, repeatInd + length), repeatInd, length);
            } else { //if no matches are found, update index
                repeatInd = i;
            }
            targetInd = repeatInd - 1;
        }
        //save any unsaved palindrome to list
        if (palindrome != null) palindromes.add(palindrome);
    }
}
