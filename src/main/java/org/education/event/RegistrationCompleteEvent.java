package org.education.event;

import lombok.Getter;
import lombok.Setter;
import org.education.entity.Student;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private Student student;
    private String confirmationUrl;
    public RegistrationCompleteEvent(Student student, String confirmationUrl) {
        super(student);
        this.student = student;
        this.confirmationUrl = confirmationUrl;
    }
}
