/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.RawMetric;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Methilliev
 */
@Local
public interface IRawMetricProvider {
   public List<RawMetric> getRawMetricFromDevice(String deviceId, int number);
}
