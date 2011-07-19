/*******************************************************************************
 * Copyright 2011(c) OBiBa. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.magma;

import org.obiba.magma.Datasource;
import org.obiba.magma.MagmaEngine;
import org.obiba.magma.ValueTable;
import org.obiba.magma.VariableEntity;
import org.obiba.magma.support.VariableEntityBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMagmaInstanceProvider implements MagmaInstanceProvider {

  private MagmaEngine magmaEngine;

  @Autowired
  public DefaultMagmaInstanceProvider(MagmaEngine magmaEngine) {
    this.magmaEngine = magmaEngine;
  }

  public Datasource getOnyxDatasource() {
    return magmaEngine.getDatasources().iterator().next();
  }

  public ValueTable getValueTable(String name) {
    return getOnyxDatasource().getValueTable(name);
  }

  public VariableEntity newParticipantEntity(String identifier) {
    return new VariableEntityBean(PARTICIPANT_ENTITY_TYPE, identifier);
  }
}