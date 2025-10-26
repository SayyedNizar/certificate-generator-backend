package com.example.local_project.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificate_batches")
@Data
public class CertificateBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institutions institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private CertificateTemplates template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private Users requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BatchStatus status;

    @Column(name = "request_timestamp", nullable = false)
    private LocalDateTime requestTimestamp;

    @Column(name = "completion_timestamp")
    private LocalDateTime completionTimestamp;

    @Column(name = "successful_records")
    private Integer successfulRecords;

    @Column(name = "failed_records")
    private Integer failedRecords;
}