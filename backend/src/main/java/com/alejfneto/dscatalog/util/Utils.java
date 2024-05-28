package com.alejfneto.dscatalog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alejfneto.dscatalog.entities.Product;
import com.alejfneto.dscatalog.projections.IdProjection;
import com.alejfneto.dscatalog.projections.ProductProjection;

public class Utils {

	public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {
		List<IdProjection<ID>> result = new ArrayList<>();
		Map <ID, IdProjection<ID>> map = new HashMap<>();
		for (IdProjection<ID> p : unordered) {
			map.put(p.getId(), p);
		}
		for (IdProjection<ID> p : ordered) {
			result.add(map.get(p.getId()));
		}
		return result;
	}

}
