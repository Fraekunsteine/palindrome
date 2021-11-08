import java.util.*;

public class PalindromeTester {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        //Generate list of palindromes from string
        List<Palindrome> palindromes = new ArrayList<Palindrome>();
        new PalindromeTester().findPalindromes(input, palindromes, new ArrayList<Character>(), -1, 0, 0);

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
                //Check for subsets of palindromes as duplicates
                Palindrome overlapLeft = null;
                if (duplicates[p.getIndex()] > 0) overlapLeft = palindromes.get(duplicates[p.getIndex()] - 1);
                Palindrome overlapRight = null;
                if (duplicates[p.getIndex() + p.getLength() - 1] > 0) overlapRight = palindromes.get(duplicates[p.getIndex() + p.getLength() - 1] - 1);
                if(overlapLeft != null && overlapRight != null &&
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
    static String visualString(String s, int index, int tempInd, int targetInd) {
        String ret = "";
        for(int i = 0; i < s.length(); i++) {
            if(i == targetInd) ret += "(";
            if(i == tempInd) ret +="{";
            if(i == index) ret += "[";
            ret += s.charAt(i);
            if(i != index && i != tempInd && i != targetInd) ret += " ";
            if(i == index) ret += "] ";
            if(i == tempInd) ret +="} ";
            if(i == targetInd) ret += ") ";
        }
        return ret;
    }

    private void findPalindromes(String str, List<Palindrome> palindromes, List<Character> lookup, int targetInd, int tempInd, int i) {
        Palindrome palindrome = null;
        int length = 0;
        for (; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!lookup.isEmpty()) {
                char temp = lookup.get(tempInd), target = ' ';
                if (targetInd > -1) target = lookup.get(targetInd);

                //three checks will be done (in order of potential yield in terms of palindrome length):
                // 1) index (targetInd) of the trailing letter to look for on opposite end of palindrome (option 1 of 2)
                // 2) index directly preceding tempInd (option 2 of 2)
                // 3) index (tempInd) directly preceding current index (repeating letters)

                if (target == c) { //check for targetInd
                    length = i - targetInd + 1;
                    //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
                    if (palindrome != null && !(palindrome.getIndex() >= targetInd && palindrome.getIndex() + palindrome.getLength() <= targetInd + length)) {
                        palindromes.add(palindrome);
                    }
                    palindrome = new Palindrome(str.substring(targetInd, targetInd + length), targetInd, length);

                    targetInd--; //decrement targetInd to look for the next letter in the potential palindrome
                    tempInd = i; //update tempInd to current index
                } else if (temp == c) { //check for tempInd
                    length = i - tempInd + 1;
                    //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
                    if (palindrome != null && !(palindrome.getIndex() >= tempInd && palindrome.getIndex() + palindrome.getLength() <= tempInd + length)) {
                        palindromes.add(palindrome);
                    }
                    palindrome = new Palindrome(str.substring(tempInd, tempInd + length), tempInd, length);

                    //targetInd is mismatched so set it to tempInd - 1
                    targetInd = tempInd - 1;
                } else if (tempInd > 0 && lookup.get(tempInd - 1) == c) { //check for index preceding tempInd
                    findPalindromes(str, palindromes, lookup, tempInd - 1, tempInd, i);
                } else { //if no matches are found, save the currently found palindrome if exists
                    if (palindrome != null && !(palindrome.getIndex() >= targetInd && palindrome.getIndex() + palindrome.getLength() <= targetInd + length)) {
                        palindromes.add(palindrome);
                        palindrome = null;
                        tempInd = i; //update tempInd to current index
                    }
                    else tempInd++;
                    targetInd = tempInd - 1; //match by targetInd failed, so set targetInd to tempInd - 1
                }
            }
            //add palindrome to list if targetInd reaches the beginning of lookup
            if (targetInd == -1 && palindrome != null) {
                palindromes.add(palindrome);
                palindrome = null;
                tempInd = i; //update tempInd to current index
                targetInd = tempInd - 1;
            }
            lookup.add(c);
        }
        //save any unsaved palindrome to list
        if (palindrome != null) palindromes.add(palindrome);
    }
}
