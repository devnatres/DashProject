package com.devnatres.dashproject.animations;

import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public interface IAnimCreator {
    DnaAnimation create(HyperStore hyperStore);
}
