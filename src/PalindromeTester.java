import java.util.*;

public class PalindromeTester {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        //Generate list of palindromes from string
        List<Palindrome> palindromes = new PalindromeTester().findPalindromes(input);

        //Sort palindromes with custom comparator
        Collections.sort(palindromes, new PalindromeComparator());
        int[] duplicates = new int[input.length()+1];
        int id = 1;
        for (Palindrome p : palindromes) {
            //Check for subsets of palindromes as duplicates
            if (duplicates[p.getIndex()] == 0 || duplicates[p.getIndex() + p.getLength()] != duplicates[p.getIndex()]) {
                System.out.printf("%s,%d,%d\n", p.getContent(), p.getIndex(), p.getLength());
                for(int i = p.getIndex(); i < p.getIndex() + p.getLength(); i++) {
                    duplicates[i] = id;
                }
                id++;
            }
        }
        sc.close();
    }

    private List<Palindrome> findPalindromes(String str) {
        List<Palindrome> palindromes = new ArrayList<Palindrome>();
        List<Character> lookup = new ArrayList<Character>();
        Palindrome palindrome = null;
        int length = 0;
        for (int i = 0, tempInd = 0, targetInd = -1; i < str.length(); i++) {
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
                    tempInd++;
                } else if (temp == c) { //check for tempInd
                    length = i - tempInd + 1;
                    //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
                    if (palindrome != null && !(palindrome.getIndex() >= tempInd && palindrome.getIndex() + palindrome.getLength() <= tempInd + length)) {
                        palindromes.add(palindrome);
                    }
                    palindrome = new Palindrome(str.substring(tempInd, tempInd + length), tempInd, length);

                    //since character at targetInd == tempInd - 1, original targetInd is mismatched so set it to index directly preceding current tempInd
                    if(target == lookup.get(tempInd - 1)) targetInd = tempInd - 1;
                } else if (tempInd > 1 && lookup.get(tempInd - 1) == c) { //check for index preceding tempInd
                    length = i - tempInd + 2;
                    //check if current palindrome is a subset of the newly found palindrome and save to list if it is not
                    if (palindrome != null && !(palindrome.getIndex() >= tempInd - 1 && palindrome.getIndex() + palindrome.getLength() <= tempInd - 1 + length)) {
                        palindromes.add(palindrome);
                    }
                    palindrome = new Palindrome(str.substring(tempInd - 1, tempInd - 1 + length), tempInd - 1, length);

                    targetInd = tempInd - 2; //since tempInd - 1 is chosen over targetInd, set targetInd to tempInd - 1 and decrement
                    tempInd++;
                } else { //if no matches are found, save the currently found palindrome if exists
                    if (palindrome != null && !(palindrome.getIndex() >= targetInd && palindrome.getIndex() + palindrome.getLength() <= targetInd + length)) {
                        palindromes.add(palindrome);
                        palindrome = null;
                        tempInd = i; //update tempInd to current index
                    }
                    else tempInd++;
                    targetInd = tempInd - 1; //match by targetInd failed, so set targetInd to index directly preceding tempInd
                }
            }
            //add palindrome tp list if targetInd reaches the beginning of lookup
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
        return palindromes;
    }
}
