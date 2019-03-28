/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.LoanInterest;
import java.util.Comparator;

/**
 *
 * @author nam_t
 */
public class LoanTermComparator implements Comparator<LoanInterest> {
    @Override
    public int compare(LoanInterest l1, LoanInterest l2) {
        return l1.getTerm().compareTo(l2.getTerm());
    }
}
