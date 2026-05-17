package com.project.cmb.projection;

import org.springframework.data.rest.core.config.Projection;
import com.project.cmb.entity.ProductLine;

@Projection(name = "productLineList", types = { ProductLine.class })
public interface ProductLineListView {
    String getProductLine();
    String getTextDescription();
}