//
// Based on suggestions in https://dev.to/gualtierofr/remote-images-in-swiftui-49jp
//

import Combine
import SwiftUI

struct ImageView: View {
    @ObservedObject var imageLoader:ImageLoader
    
    @State var image:UIImage = UIImage()
    
    init(withURL url:URL?) {
        imageLoader = ImageLoader(url: url)
    }
    
    var body: some View {
        VStack {
            Image(uiImage: image)
                .resizable()
            
        }.onReceive(imageLoader.didChange) { data in
            self.image = UIImage(data: data) ?? UIImage()
        }
    }
}

struct ImageView_Previews: PreviewProvider {
    static var previews: some View {
        ImageView(withURL: nil)
    }
}
