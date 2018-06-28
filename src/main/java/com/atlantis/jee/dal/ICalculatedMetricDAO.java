/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.dal;

import com.atlantis.jee.model.CalculatedMetric;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Methilliev
 */
@Local
public interface ICalculatedMetricDAO {
     public void create(CalculatedMetric calculatedMetric) throws Exception ;
     public void update(CalculatedMetric calculatedMetric) throws Exception ;
     public void delete(CalculatedMetric calculatedMetric) throws Exception ;
     public List<CalculatedMetric> findByDeviceId(String id) throws Exception ;
}
