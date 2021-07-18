//
// Based on suggestions in https://dev.to/gualtierofr/remote-images-in-swiftui-49jp
//

import Combine
import SwiftUI

public struct PetAdoptImageView: View {
    @ObservedObject var imageLoader:ImageLoader
    @State var image:UIImage = UIImage(systemName: "photo.fill") ?? UIImage()
    
    public init(withURL url:URL?) {
        imageLoader = ImageLoader(url: url)
    }
    
    public var body: some View {
        VStack {
            Image(uiImage: image)
                .resizable()
        }.onReceive(imageLoader.didChange) { data in
            self.image = UIImage(data: data) ?? UIImage(systemName: "photo.fill")!
        }
    }
}

struct PetAdoptImageView_Previews: PreviewProvider {
    static var previews: some View {
        PetAdoptImageView(withURL: nil)
    }
}
