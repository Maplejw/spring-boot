package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrCondition extends NoTableNameCondition {
	private static final long serialVersionUID = -1175065710839607420L;
	private List<Condition> components = new ArrayList<>();

	public OrCondition() {
	}

	public OrCondition(Condition[] components) {
		Condition[] arg1 = components;
		int arg2 = components.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Condition component = arg1[arg3];
			this.add(component);
		}

	}

	public List<Condition> getComponents() {
		return this.components;
	}

	public void setComponents(List<Condition> components) {
		this.components = components;
	}

	public Map<String, Object> getParameters() {
		if (this.components.isEmpty()) {
			return ALWAYS_FALSE_CONDITION.getParameters();
		} else if (this.components.size() == 1) {
			return ((Condition) this.components.get(0)).getParameters();
		} else {
			Map<String, Object> parms = new HashMap<>();
			Iterator<Condition> arg1 = this.components.iterator();

			while (arg1.hasNext()) {
				Condition component = (Condition) arg1.next();
				parms.putAll(component.getParameters());
			}

			return parms;
		}
	}

	public String toSqlString() {
		if (this.components.isEmpty()) {
			return ALWAYS_FALSE_CONDITION.toSqlString();
		} else if (this.components.size() == 1) {
			return ((Condition) this.components.get(0)).toSqlString();
		} else {
			StringBuilder sb = new StringBuilder("(" + ((Condition) this.components.get(0)).toSqlString() + ")");

			for (int i = 1; i < this.components.size(); ++i) {
				sb.append(" OR (" + ((Condition) this.components.get(i)).toSqlString() + ")");
			}

			return sb.toString();
		}
	}

	public Condition add(Condition component) {
		this.components.add(component);
		return this;
	}
}