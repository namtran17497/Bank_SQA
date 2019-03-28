/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.SavingInterest;
import java.util.Comparator;

/**
 *
 * @author nam_t
 */
public class SavingTermComparator implements Comparator<SavingInterest> {
    @Override
    public int compare(SavingInterest s1, SavingInterest s2) {
        return s1.getTerm().compareTo(s2.getTerm());
    }
}
