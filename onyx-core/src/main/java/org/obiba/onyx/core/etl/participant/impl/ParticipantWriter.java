/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.core.etl.participant.impl;

import org.obiba.onyx.core.domain.participant.Participant;
import org.springframework.batch.item.database.HibernateItemWriter;

/**
 * Spring Batch ItemWriter for Participant items, used in the second step of Appointment List Update job.
 */
public class ParticipantWriter extends HibernateItemWriter<Participant> {

}
