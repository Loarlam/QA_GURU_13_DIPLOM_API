package in.reqres.models.lombok;

import lombok.Data;

@Data
public class UpdateUserResponseLombokModel {
    private String updateName,
            updateJob,
            updatedAt;
}
