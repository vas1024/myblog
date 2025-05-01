package myblog.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TextPreviewSize {
  public final int previewSize;
  public TextPreviewSize (@Value("${text.preview.size:100}") int previewSize) {
    this.previewSize = previewSize;
  }

}

