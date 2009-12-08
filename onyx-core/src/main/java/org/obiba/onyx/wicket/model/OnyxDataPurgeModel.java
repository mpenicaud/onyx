/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.wicket.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.obiba.onyx.core.domain.participant.InterviewStatus;
import org.obiba.onyx.core.domain.participant.Participant;
import org.obiba.onyx.core.domain.statistics.ExportLog;
import org.obiba.onyx.core.service.ExportLogService;
import org.obiba.onyx.engine.variable.export.OnyxDataPurge;

/**
 * Model to obtain details of the Participants that are scheduled to be purged. The load method obtains all the
 * Participants to be purged. Other methods provide access to high level details about those Participants.
 */
public class OnyxDataPurgeModel extends SpringDetachableModel<OnyxDataPurge> {
  private static final long serialVersionUID = 1L;

  private int totalInterviewsToPurge;

  private List<Participant> exportedInterviews;

  private List<Participant> unexportedInterviews;

  @SpringBean
  private OnyxDataPurge onyxDataPurge;

  @SpringBean
  private ExportLogService exportLogService;

  public OnyxDataPurgeModel() {
    load();
  }

  @Override
  protected OnyxDataPurge load() {
    List<Participant> participantsToPurge = onyxDataPurge.getParticipantsToPurge();
    totalInterviewsToPurge = participantsToPurge.size();
    exportedInterviews = new ArrayList<Participant>();
    unexportedInterviews = new ArrayList<Participant>();
    for(Participant participant : participantsToPurge) {
      List<ExportLog> exportLogs = exportLogService.getExportLogs("Participant", participant.getBarcode(), null, true);
      if(exportLogs == null || exportLogs.isEmpty()) {
        unexportedInterviews.add(participant);
      } else {
        exportedInterviews.add(participant);
      }
    }
    return onyxDataPurge;
  }

  public String getTotalInterviewsToPurge() {
    return Integer.toString(totalInterviewsToPurge);
  }

  public String getTotalExportedInterviewsToPurge() {
    return Integer.toString(exportedInterviews.size());
  }

  public String getTotalUnexportedInterviewsToPurge() {
    return Integer.toString(unexportedInterviews.size());
  }

  public String getTotalUnexportedInterviewsWithStatus(InterviewStatus status) {
    int result = 0;
    for(Participant participant : unexportedInterviews) {
      if(participant.getInterview().getStatus() == status) result++;
    }
    return Integer.toString(result);
  }

  public String getTotalExportedInterviewsWithStatus(InterviewStatus status) {
    int result = 0;
    for(Participant participant : exportedInterviews) {
      if(participant.getInterview().getStatus() == status) result++;
    }
    return Integer.toString(result);
  }

}
